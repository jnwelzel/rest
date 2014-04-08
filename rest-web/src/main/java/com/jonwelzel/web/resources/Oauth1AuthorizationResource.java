package com.jonwelzel.web.resources;

import static com.jonwelzel.web.oauth.OAuth1Configuration.AUTHORIZATION_ROOT_URL;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import org.glassfish.jersey.server.mvc.Viewable;
import org.slf4j.Logger;

import com.jonwelzel.commons.entities.Consumer;
import com.jonwelzel.commons.entities.Token;
import com.jonwelzel.commons.entities.User;
import com.jonwelzel.commons.utils.SecurityUtils;
import com.jonwelzel.ejb.annotations.Log;
import com.jonwelzel.ejb.consumer.ConsumerBean;
import com.jonwelzel.ejb.oauth.AuthTokenBean;
import com.jonwelzel.ejb.session.HttpSessionBean;
import com.jonwelzel.ejb.user.UserBean;
import com.jonwelzel.web.oauth.OAuth1Exception;

@Path(AUTHORIZATION_ROOT_URL)
public class Oauth1AuthorizationResource {

	@Inject
	@Log
	private Logger log;

	@Inject
	private UserBean userBean;

	@Inject
	private HttpSessionBean httpSessionBean;

	@Inject
	private AuthTokenBean authTokenBean;

	@Inject
	private ConsumerBean consumerBean;

	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response authorize(@FormParam("oauthToken") String oauthToken, @FormParam("sessionToken") String sessionToken) {
		if (oauthToken == null || "".equals(oauthToken) || sessionToken == null || "".equals(sessionToken)) {
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST)
					.entity("\"oauthToken\" and \"sessionToken\" paramaters are mandatory.").build());
		}
		// Find token first
		Token authorized = authTokenBean.find(oauthToken);
		if (authorized == null) {
			throw new WebApplicationException(Response.status(Status.NOT_FOUND)
					.entity("Token \"" + oauthToken + "\" not found.").build());
		}
		Long userId = Long.valueOf(httpSessionBean.getUserId(sessionToken));
		if (userId == null) {
			throw new WebApplicationException(Response.status(Status.NOT_FOUND)
					.entity("Could not find user with session id \"" + sessionToken + "\".").build());
		}
		User user = userBean.findUser(userId);
		try {
			// Add verfier to token, save it
			String verifier = SecurityUtils.generateSecureHex();
		} catch (NoSuchAlgorithmException e) {
			log.error(null, e);
			throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity("Could not generate verifier code for the token.").build());
		}
		// Append verifier to callback URL and dispatch the response
		return null;
	}

	@GET
	public Viewable getHtml(@QueryParam("oauth_token") String oauthToken,
			@QueryParam("session_token") String sessionToken) {
		if (oauthToken == null || "".equals(oauthToken)) {
			throw new OAuth1Exception("\"oauth_token\" must be informed as a query parameter.");
		}
		Consumer consumer = consumerBean.findByToken(oauthToken);
		if (sessionToken == null || "".equals(sessionToken)) {
			Map<String, String> model = new HashMap<>();
			model.put("consumerApplicationName", consumer.getApplicationName());
			model.put("consumerDomain", consumer.getApplicationUrl());
			model.put("oauthToken", oauthToken);
			return new Viewable("/authorization/login", model);
		} else {
			Long userId = Long.valueOf(httpSessionBean.getUserId(sessionToken));
			User dbUser = userBean.findUser(userId);
			Map<String, Object> model = new HashMap<>();
			model.put("consumerApplicationName", consumer.getApplicationName());
			model.put("consumerDomain", consumer.getApplicationUrl());
			model.put("consumerApplicationDescription", consumer.getApplicationDescription());
			model.put("oauthToken", oauthToken);
			model.put("sessionToken", sessionToken);
			model.put("user", dbUser);
			return new Viewable("/authorization/authorize", model);
		}
	}

	@Path("login")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(User user, @QueryParam("oauth_token") String oauthToken,
			@Context SecurityContext securityContext) {
		if (oauthToken == null || "".equals(oauthToken)) {
			throw new OAuth1Exception("\"oauth_token\" must be informed as a query parameter.");
		}

		User dbUser = userBean.findByEmail(user.getEmail());
		if (dbUser == null) {
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity("Invalid email address.")
					.build());
		}
		if (!SecurityUtils.validatePassword(user.getPassword(), dbUser.getPasswordHash())) {
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity("Invalid password.").build());
		}
		try { // Now the session stuff
			String hash = SecurityUtils.generateSecureHex();
			httpSessionBean.newSession(hash, dbUser.getId().toString(), false);
			Map<String, Object> model = new HashMap<>();
			model.put("oauthToken", oauthToken);
			model.put("sessionToken", hash);
			return Response.status(Status.OK).entity(model).build();
		} catch (NoSuchAlgorithmException e) {
			log.error("The \"SHA1\" encryption algorithm needed to generate the user session hash could not be found.");
			throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity("Could not secure user session.").build());
		}
	}

}
