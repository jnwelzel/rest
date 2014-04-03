package com.jonwelzel.web.resources;

import static com.jonwelzel.web.oauth.OAuth1IdentityApiEndpoints.AUTHORIZATION_ROOT_URL;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.glassfish.jersey.server.mvc.Viewable;

@Path(AUTHORIZATION_ROOT_URL)
public class Oauth1AuthorizationResource {

    @POST
    public Response authorize() {
        return null;
    }

    @GET
    public Viewable getHtml(@Context SecurityContext securityContext) {
        if (securityContext.getUserPrincipal() == null) {
            return new Viewable("/authorization/login.ftl", "token");
        } else {
            return new Viewable("/authorization/authorize.ftl", null);
        }
    }

}
