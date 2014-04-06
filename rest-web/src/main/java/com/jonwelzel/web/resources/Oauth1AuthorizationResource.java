package com.jonwelzel.web.resources;

import static com.jonwelzel.web.oauth.OAuth1Endpoints.AUTHORIZATION_ROOT_URL;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import org.glassfish.jersey.server.mvc.Viewable;
import org.slf4j.Logger;

import com.jonwelzel.ejb.annotations.Log;
import com.jonwelzel.ejb.session.HttpSessionBean;
import com.jonwelzel.ejb.user.UserBean;
import com.jonwelzel.persistence.entities.Consumer;
import com.jonwelzel.persistence.entities.User;
import com.jonwelzel.util.SecurityUtils;
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

	@POST
	public Response authorize() {
		return null;
	}

	@GET
	public Viewable getHtml(@QueryParam("oauth_token") String oauthToken, @Context SecurityContext securityContext) {
		if (oauthToken == null || "".equals(oauthToken)) {
			throw new OAuth1Exception("\"oauth_token\" must be informed as a query parameter.");
		}
		Map<String, String> model = new HashMap<>();
		model.put("consumerApplicationName", ((Consumer) securityContext.getUserPrincipal()).getApplicationName());
		model.put("consumerDomain", ((Consumer) securityContext.getUserPrincipal()).getApplicationUrl());
		model.put("oauthToken", oauthToken);
		return new Viewable("/authorization/login", model);
	}

	@Path("login")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Viewable login(User user, @QueryParam("oauth_token") String oauthToken,
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
			dbUser.setPassword(hash); // send back the session secret and not the user id, use the 'password' attr for
										// storing the session hash, that's all ;)
		} catch (NoSuchAlgorithmException e) {
			log.error("The \"SHA1\" encryption algorithm needed to generate the user session hash could not be found.");
			throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity("Could not secure user session.").build());
		}

		Map<String, Object> model = new HashMap<>();
		model.put("consumerApplicationName", ((Consumer) securityContext.getUserPrincipal()).getApplicationName());
		model.put("consumerDomain", ((Consumer) securityContext.getUserPrincipal()).getApplicationUrl());
		model.put("consumerApplicationDescription",
				((Consumer) securityContext.getUserPrincipal()).getApplicationDescription());
		model.put("oauthToken", oauthToken);
		model.put("user", dbUser);
		return new Viewable("/authorization/authorize", model);
	}

}
