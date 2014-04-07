package com.jonwelzel.ejb.oauth;

import java.security.NoSuchAlgorithmException;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.jonwelzel.commons.entities.Token;
import com.jonwelzel.commons.entities.Consumer;
import com.jonwelzel.commons.entities.OAuth1Token;
import com.jonwelzel.commons.utils.SecurityUtils;
import com.jonwelzel.ejb.annotations.Log;
import com.jonwelzel.ejb.consumer.ConsumerDao;

@Stateless(mappedName = "AuthTokenBean")
@LocalBean
public class AuthTokenBean {

    @Inject
    @Log
    private Logger log;

    @EJB
    private AuthTokenDao tokenDao;

    @EJB
    private ConsumerDao consumerDao;

    public Token find(String id) {
        return tokenDao.find(id);
    }

    /**
     * Creates a new request token for a given consumerKey.
     * 
     * @param consumerKey
     *            consumer key to create a request token for
     * @param callbackUrl
     *            callback url for this request token request
     * @return new request token
     */
    public Token newRequestToken(String consumerKey, String callbackUrl) {
        Token token = null;
        Consumer consumer = consumerDao.findByKey(consumerKey);
        if (consumer != null) {
            try {
                token = new Token(SecurityUtils.generateSecureHex(), SecurityUtils.generateSecureHex(), consumer,
                        callbackUrl);
                token.setConsumer(consumer);
                token.setCallbackUrl(callbackUrl);
                token = tokenDao.save(token);
            } catch (NoSuchAlgorithmException e) {
                log.error("Algorithm not found, could not generate secure hash.", e);
            }
        }
        return token;
    }

    public Token newAccessToken(OAuth1Token rt, String verifier) {
        // TODO Auto-generated method stub
        return null;
    }
}
