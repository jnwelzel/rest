package com.jonwelzel.web.oauth;

import java.util.Arrays;
import java.util.List;

public class OAuth1Endpoints {

	public static final String AUTHORIZATION_ROOT_URL = "/authorization";
	public static final String TOKEN_ROOT_URL = "/token";
	public static final String CONSUMER_ROOT_URL = "/consumers";
	public static final String REQUEST_TOKEN_URL = "request";
	public static final String ACCESS_TOKEN_URL = "access";

	/** Resource paths that should be ignored by the OAuth filter */
	public static final List<String> pathsToIgnore = Arrays.asList(TOKEN_ROOT_URL + "/" + ACCESS_TOKEN_URL,
			TOKEN_ROOT_URL + "/" + REQUEST_TOKEN_URL, AUTHORIZATION_ROOT_URL);
}
