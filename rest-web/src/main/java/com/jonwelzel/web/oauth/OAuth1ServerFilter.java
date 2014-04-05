package com.jonwelzel.web.oauth;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;

import com.jonwelzel.ejb.annotations.Log;
import com.jonwelzel.ejb.consumer.ConsumerBean;
import com.jonwelzel.ejb.oauth.AuthTokenBean;
import com.jonwelzel.ejb.session.HttpSessionBean;
import com.jonwelzel.ejb.user.UserBean;
import com.jonwelzel.persistence.entities.AuthToken;
import com.jonwelzel.persistence.entities.Consumer;
import com.jonwelzel.persistence.entities.User;
import com.jonwelzel.web.security.SecurityContextImpl;

/**
 * A filter class for authenticating requests.
 * 
 * 
 * The role of this filter class is to set a
 * {@link javax.ws.rs.core.SecurityContext} in the
 * {@link ContainerRequestFilter}
 * 
 * @see {@link com.jonwelzel.web.security.SecurityContextImpl}
 * 
 * @author jwelzel
 */
@Provider
@Priority(Priorities.AUTHENTICATION)
public class OAuth1ServerFilter implements ContainerRequestFilter {

	@Inject
	@Log
	private Logger log;

	@Inject
	private HttpSessionBean httpSessionBean;

	@Inject
	private UserBean userBean;

	@Inject
	private ConsumerBean consumerBean;

	@Inject
	private AuthTokenBean authTokenBean;

	@Inject
	private OAuth1Signature oAuth1Signature;

	private static final String REALM = "https://localhost:8181/rest";

	/** Value to return in www-authenticate header when 401 response returned. */
	private final String wwwAuthenticateHeader;

	/** OAuth protocol versions that are supported. */
	private final Set<String> versions;

	/** Manages and validates incoming nonces. */
	private final NonceManager nonces;

	/**
	 * Keeps the authorization header value for further use by the class
	 * methods.
	 */
	private String authHeader;

	private String oauthToken;

	public OAuth1ServerFilter() {
		// establish supported OAuth protocol versions
		HashSet<String> v = new HashSet<String>();
		v.add(null);
		v.add("1.0");
		versions = Collections.unmodifiableSet(v);
		nonces = new NonceManager();
		wwwAuthenticateHeader = "OAuth realm=\"" + REALM + "\"";
	}

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		SecurityContext securityContext = new SecurityContextImpl(null);
		authHeader = requestContext.getHeaderString(OAuth1Parameters.AUTHORIZATION_HEADER);

		if (requestContext.getUriInfo().getQueryParameters().containsKey(OAuth1Parameters.TOKEN)) {
			oauthToken = requestContext.getUriInfo().getQueryParameters().get(OAuth1Parameters.TOKEN).get(0);
		}

		// do not filter requests that do not use OAuth authentication
		if (authHeader != null && authHeader.toUpperCase().startsWith(OAuth1Parameters.SCHEME.toUpperCase())) {
			securityContext = filterOAuth(requestContext);
		} else if (authHeader != null) {
			securityContext = filterOther(requestContext);
		} else if (oauthToken != null && !"".equals(oauthToken)) {
			requestContext.setSecurityContext(new OAuth1SecurityContext(consumerBean.findByToken(oauthToken), false));
			return;
		}

		requestContext.setSecurityContext(securityContext);
	}

	private SecurityContext filterOAuth(ContainerRequestContext requestContext) {

		// do not filter requests that match to access or token resources or if the request path matches pattern to ignore
		if (OAuth1Endpoints.pathsToIgnore.contains(requestContext.getUriInfo().getPath())) {
			return new SecurityContextImpl(null);
		}

		OAuth1SecurityContext sc;
		try {
			sc = getSecurityContext(requestContext);
		} catch (OAuth1Exception e) {
			throw e;
		}

		return sc;
	}

	private SecurityContext filterOther(ContainerRequestContext requestContext) {
		User user = null;
		if (authHeader != null) {
			final String userId = httpSessionBean.getUserId(authHeader);
			if (userId == null) {
				// Ops, session expired buddy
				requestContext.abortWith(Response.status(Status.UNAUTHORIZED).entity("Session expired!").build());
			} else {
				user = userBean.findUser(Long.valueOf(userId));
			}
		}
		log.info("[" + requestContext.getRequest().getMethod() + "] \"" + requestContext.getUriInfo().getPath()
				+ "\" (User=" + user + ", token=" + authHeader + ")");
		return new SecurityContextImpl(user);
	}

	private OAuth1SecurityContext getSecurityContext(ContainerRequestContext request) throws OAuth1Exception {
		OAuthServerRequest osr = new OAuthServerRequest(request);
		OAuth1Parameters params = new OAuth1Parameters().readRequest(osr);

		// apparently not signed with any OAuth parameters; unauthorized
		if (params.size() == 0) {
			throw newUnauthorizedException();
		}

		// get required OAuth parameters
		String consumerKey = requiredOAuthParam(params.getConsumerKey());
		String token = params.getToken();
		String timestamp = requiredOAuthParam(params.getTimestamp());
		String nonce = requiredOAuthParam(params.getNonce());

		// enforce other supported and required OAuth parameters
		requiredOAuthParam(params.getSignature());
		supportedOAuthParam(params.getVersion(), versions);

		// retrieve secret for consumer key
		Consumer consumer = consumerBean.findByKey(consumerKey);
		if (consumer == null) {
			throw newUnauthorizedException();
		}

		OAuth1Secrets secrets = new OAuth1Secrets().consumerSecret(consumer.getSecret());
		OAuth1SecurityContext sc;
		String nonceKey;

		if (token == null) {
			if (consumer == null) {
				throw newUnauthorizedException();
			}
			nonceKey = "c:" + consumerKey;
			sc = new OAuth1SecurityContext(consumer, request.getSecurityContext().isSecure());
		} else {
			AuthToken accessToken = authTokenBean.find(token);
			if (accessToken == null) {
				throw newUnauthorizedException();
			}

			Consumer atConsumer = accessToken.getConsumer();
			if (atConsumer == null || !consumerKey.equals(atConsumer.getKey())) {
				throw newUnauthorizedException();
			}

			nonceKey = "t:" + token;
			secrets.tokenSecret(accessToken.getSecret());
			sc = new OAuth1SecurityContext(accessToken, request.getSecurityContext().isSecure());
		}

		if (!verifySignature(osr, params, secrets)) {
			throw newUnauthorizedException();
		}

		if (!nonces.verify(nonceKey, timestamp, nonce)) {
			throw newUnauthorizedException();
		}

		return sc;
	}

	private static String requiredOAuthParam(String value) throws OAuth1Exception {
		if (value == null) {
			throw newBadRequestException();
		}
		return value;
	}

	private static String supportedOAuthParam(String value, Set<String> set) throws OAuth1Exception {
		if (!set.contains(value)) {
			throw newBadRequestException();
		}
		return value;
	}

	private boolean verifySignature(OAuthServerRequest osr, OAuth1Parameters params, OAuth1Secrets secrets) {
		try {
			return oAuth1Signature.verify(osr, params, secrets);
		} catch (OAuth1SignatureException ose) {
			throw newBadRequestException();
		}
	}

	private static OAuth1Exception newBadRequestException() throws OAuth1Exception {
		return new OAuth1Exception(Response.Status.BAD_REQUEST, null);
	}

	private OAuth1Exception newUnauthorizedException() throws OAuth1Exception {
		return new OAuth1Exception(Response.Status.UNAUTHORIZED, wwwAuthenticateHeader);
	}

}
