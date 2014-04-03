package com.jonwelzel.web.resources;

import static com.jonwelzel.web.oauth.OAuth1IdentityApiEndpoints.ACCESS_TOKEN_URL;
import static com.jonwelzel.web.oauth.OAuth1IdentityApiEndpoints.REQUEST_TOKEN_URL;
import static com.jonwelzel.web.oauth.OAuth1IdentityApiEndpoints.TOKEN_ROOT_URL;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path(TOKEN_ROOT_URL)
public class OAuth1TokenResource {

    @Path(REQUEST_TOKEN_URL)
    public Response requestToken() {
        return null;
    }

    @Path(ACCESS_TOKEN_URL)
    public Response accessToken() {
        return null;
    }

}
