package com.jonwelzel.web.oauth;

import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.Token;

public class OAuth1IdentityApi extends DefaultApi10a {

    public static final String AUTHORIZATION_ROOT_URL = "/authorize";
    public static final String TOKEN_ROOT_URL = "/token";
    public static final String REQUEST_TOKEN_URL = "request";
    public static final String ACCESS_TOKEN_URL = "access";

    @Override
    public String getAccessTokenEndpoint() {
        return TOKEN_ROOT_URL + "/" + ACCESS_TOKEN_URL;
    }

    @Override
    public String getRequestTokenEndpoint() {
        return TOKEN_ROOT_URL + "/" + REQUEST_TOKEN_URL;
    }

    @Override
    public String getAuthorizationUrl(Token token) {
        return AUTHORIZATION_ROOT_URL;
    }

}
