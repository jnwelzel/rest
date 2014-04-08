package com.jonwelzel.ejb.oauth;

import java.security.NoSuchAlgorithmException;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.jonwelzel.commons.entities.Consumer;
import com.jonwelzel.commons.entities.Token;
import com.jonwelzel.commons.utils.SecurityUtils;
import com.jonwelzel.ejb.annotations.Log;
import com.jonwelzel.ejb.consumer.ConsumerDao;

@Stateless(mappedName = "AuthTokenBean")
@LocalBean
public class TokenBean {

    @Inject
    @Log
    private Logger log;

    @EJB
    private TokenDao tokenDao;

    @EJB
    private ConsumerDao consumerDao;

    public Token findByToken(String token) {
        return tokenDao.findByToken(token);
    }

    public Token find(Long id) {
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
                token.setCallbackUrl(callbackUrl != null && !"".equals(callbackUrl) ? callbackUrl : consumer
                        .getApplicationCallbackUrl());
                token = tokenDao.save(token);
            } catch (NoSuchAlgorithmException e) {
                log.error("Algorithm not found, could not generate secure hash.", e);
            }
        }
        return token;
    }

    public Token newAccessToken(Token requestToken, String verifier) {
        // TODO Take the request token, set new 'secret' and new 'token'
        return tokenDao.save(requestToken);
    }

    public Token save(Token token) {
        return tokenDao.save(token);
    }
}
