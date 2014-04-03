package com.jonwelzel.web.resources;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.jonwelzel.web.oauth.OAuth1IdentityApi;

@Path(OAuth1IdentityApi.AUTHORIZATION_ROOT_URL)
public class Oauth1AuthorizationResource {

    @POST
    public Response authorize() {
        return null;
    }

}
