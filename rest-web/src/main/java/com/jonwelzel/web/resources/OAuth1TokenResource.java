package com.jonwelzel.web.resources;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.jonwelzel.web.oauth.OAuth1IdentityApi;

@Path(OAuth1IdentityApi.TOKEN_ROOT_URL)
public class OAuth1TokenResource {

    @Path(OAuth1IdentityApi.REQUEST_TOKEN_URL)
    public Response requestToken() {
        return null;
    }

    @Path(OAuth1IdentityApi.ACCESS_TOKEN_URL)
    public Response accessToken() {
        return null;
    }

}
