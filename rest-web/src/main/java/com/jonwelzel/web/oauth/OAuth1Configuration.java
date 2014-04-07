package com.jonwelzel.web.oauth;

import java.util.Arrays;
import java.util.List;

public class OAuth1Configuration {

    public static final String AUTHORIZATION_ROOT_URL = "/authorization";
    public static final String REQUEST_TOKEN_URL = "/requestToken";
    public static final String ACCESS_TOKEN_URL = "/accessToken";

    public static final String REALM = "https://localhost:8181/rest";

    /** Resource paths that should be ignored by the {@link SessionServerFilter} filter */
    public static final List<String> pathsToIgnore = Arrays.asList(ACCESS_TOKEN_URL, REQUEST_TOKEN_URL,
            AUTHORIZATION_ROOT_URL);
}
