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

import org.slf4j.Logger;

import com.jonwelzel.commons.entities.OAuth1Consumer;
import com.jonwelzel.commons.entities.OAuth1Token;
import com.jonwelzel.ejb.annotations.Log;

/**
 * OAuth request filter that filters all requests indicating in the Authorization header they use OAuth. Checks if the
 * incoming requests are properly authenticated and populates the security context with the corresponding user principal
 * and roles.
 * <p>
 * 
 * @author Paul C. Bryan <pbryan@sun.com>
 * @author Martin Matula
 */
@Priority(Priorities.AUTHENTICATION)
public class OAuth1ServerFilter implements ContainerRequestFilter {

    @Inject
    @Log
    private Logger log;

    /** OAuth Server */
    @Inject
    private OAuth1Provider provider;

    /** Manages and validates incoming nonces. */
    private final NonceManager nonces;

    /** Value to return in www-authenticate header when 401 response returned. */
    private final String wwwAuthenticateHeader;

    /** OAuth protocol versions that are supported. */
    private final Set<String> versions;

    @Inject
    private OAuth1Signature oAuth1Signature;

    /**
     * Create a new filter.
     */
    public OAuth1ServerFilter() {
        // establish supported OAuth protocol versions
        HashSet<String> v = new HashSet<String>();
        v.add(null);
        v.add("1.0");
        versions = Collections.unmodifiableSet(v);
        nonces = new NonceManager();
        wwwAuthenticateHeader = "OAuth realm=\"" + OAuth1Configuration.REALM + "\"";
    }

    @Override
    public void filter(ContainerRequestContext request) throws IOException {
        // do not filter requests that do not use OAuth authentication
        final String authHeader = request.getHeaderString(OAuth1Parameters.AUTHORIZATION_HEADER);
        if (authHeader == null || !authHeader.toUpperCase().startsWith(OAuth1Parameters.SCHEME.toUpperCase())) {
            return;
        }

        // do not filter requests that matches to access or token resources
        if (OAuth1Configuration.pathsToIgnore.contains(request.getUriInfo().getPath())) {
            return;
        }

        OAuth1SecurityContext sc;
        try {
            sc = getSecurityContext(request);
        } catch (OAuth1Exception e) {
            throw e;
        }

        request.setSecurityContext(sc);
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
        OAuth1Consumer consumer = provider.getConsumer(consumerKey);
        if (consumer == null) {
            throw newUnauthorizedException();
        }

        OAuth1Secrets secrets = new OAuth1Secrets().consumerSecret(consumer.getSecret());
        OAuth1SecurityContext sc;
        String nonceKey;

        if (token == null) {
            if (consumer.getPrincipal() == null) {
                throw newUnauthorizedException();
            }
            nonceKey = "c:" + consumerKey;
            sc = new OAuth1SecurityContext(consumer, request.getSecurityContext().isSecure());
        } else {
            OAuth1Token accessToken = provider.getAccessToken(token);
            if (accessToken == null) {
                throw newUnauthorizedException();
            }

            OAuth1Consumer atConsumer = accessToken.getConsumer();
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
        log.info("[" + request.getRequest().getMethod() + "] \"" + request.getUriInfo().getPath() + "\" (User="
                + sc.getUserPrincipal() + ", token=" + sc.getToken() + ")");
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
