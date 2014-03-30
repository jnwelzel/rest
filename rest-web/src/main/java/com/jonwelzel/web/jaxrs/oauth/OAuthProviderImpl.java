package com.jonwelzel.web.jaxrs.oauth;

import java.util.List;
import java.util.Map;

import javax.inject.Named;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.server.oauth1.OAuth1Consumer;
import org.glassfish.jersey.server.oauth1.OAuth1Provider;
import org.glassfish.jersey.server.oauth1.OAuth1Token;

@Provider
@Named
public class OAuthProviderImpl implements OAuth1Provider {

	@Override
	public OAuth1Consumer getConsumer(String consumerKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OAuth1Token newRequestToken(String consumerKey, String callbackUrl, Map<String, List<String>> attributes) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OAuth1Token getRequestToken(String token) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OAuth1Token newAccessToken(OAuth1Token requestToken, String verifier) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OAuth1Token getAccessToken(String token) {
		// TODO Auto-generated method stub
		return null;
	}

}
