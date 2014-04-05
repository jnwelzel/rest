package com.jonwelzel.web.resources;

import static com.jonwelzel.web.oauth.OAuth1Endpoints.AUTHORIZATION_ROOT_URL;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.glassfish.jersey.server.mvc.Viewable;

import com.jonwelzel.web.oauth.OAuth1Exception;
import com.jonwelzel.web.oauth.OAuth1Parameters;
import com.jonwelzel.web.oauth.OAuth1SecurityContext;

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
		Form form = new Form();
		form.param(OAuth1Parameters.TOKEN, oauthToken);
		form.param(OAuth1Parameters.CONSUMER_DOMAIN, ((OAuth1SecurityContext) securityContext).getConsumer()
				.getApplicationUrl());
		return new Viewable("/authorization/login.ftl", form);
		// return new Viewable("/authorization/authorize.ftl", null);
	}

}
