package com.jonwelzel.web.resources;

import static com.jonwelzel.web.oauth.OAuth1Endpoints.AUTHORIZATION_ROOT_URL;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.glassfish.jersey.server.mvc.Viewable;

import com.jonwelzel.persistence.entities.Consumer;
import com.jonwelzel.web.oauth.OAuth1Exception;

@Path(AUTHORIZATION_ROOT_URL)
public class Oauth1AuthorizationResource {

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

}
