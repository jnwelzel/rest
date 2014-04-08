package com.jonwelzel.web.oauth;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.ext.Provider;

import com.jonwelzel.commons.entities.OAuth1Consumer;
import com.jonwelzel.commons.entities.OAuth1Token;
import com.jonwelzel.commons.entities.Token;
import com.jonwelzel.ejb.consumer.ConsumerBean;
import com.jonwelzel.ejb.oauth.TokenBean;

/**
 * Glue between application-specific domain objects and OAuth-specific objects. This is the point where the application
 * domain and the OAuth domain come together.
 * 
 * @author jwelzel
 * 
 */
@Provider
public class OAuth1ProviderImpl implements OAuth1Provider {

    @Inject
    private TokenBean tokenBean;

    @Inject
    private ConsumerBean consumerBean;

    @Override
    public OAuth1Consumer getConsumer(String consumerKey) {
        return consumerBean.findByKey(consumerKey);
    }

    @Override
    public OAuth1Token newRequestToken(String consumerKey, String callbackUrl, Map<String, List<String>> attributes) {
        return tokenBean.newRequestToken(consumerKey, callbackUrl);
    }

    @Override
    public OAuth1Token getRequestToken(String token) {
        return tokenBean.findByToken(token);
    }

    @Override
    public OAuth1Token newAccessToken(OAuth1Token requestToken, String verifier) {
        Token rt = (Token) requestToken;
        return tokenBean.newAccessToken(rt, verifier);
    }

    @Override
    public OAuth1Token getAccessToken(String token) {
        return tokenBean.findByToken(token);
    }

}
