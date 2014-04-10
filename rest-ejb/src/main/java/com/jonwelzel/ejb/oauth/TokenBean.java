package com.jonwelzel.ejb.oauth;

import java.net.URI;
import java.security.NoSuchAlgorithmException;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.jonwelzel.commons.entities.Consumer;
import com.jonwelzel.commons.entities.OAuth1Token;
import com.jonwelzel.commons.entities.Token;
import com.jonwelzel.commons.entities.User;
import com.jonwelzel.commons.utils.SecurityUtils;
import com.jonwelzel.ejb.annotations.Log;
import com.jonwelzel.ejb.consumer.ConsumerDao;
import com.jonwelzel.ejb.exceptions.checked.ApplicationException;

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

    public URI authorize(Token authorized, User user) throws NoSuchAlgorithmException {
        // Add verfier and User to token, save it
        String verifier = SecurityUtils.generateSecureHex();
        authorized.setVerifier(verifier);
        authorized.setUser(user);
        authorized = tokenDao.save(authorized);
        URI callback = URI.create(authorized.getCallbackUrl() + "?oauth_token=" + authorized.getToken()
                + "&oauth_verifier=" + verifier);
        return callback;
    }

    public Token newAccessToken(OAuth1Token requestToken, String verifier) throws ApplicationException,
            NoSuchAlgorithmException {
        Token accessToken = findByToken(requestToken.getToken());
        if (!accessToken.getVerifier().equals(verifier)) {
            throw new ApplicationException("The verifier informed does not match any token.");
        }
        // Take the request token, set new 'secret' and new 'token' and u got yourself an access token
        accessToken.setSecret(SecurityUtils.generateSecureHex());
        accessToken.setToken(SecurityUtils.generateSecureHex());
        return tokenDao.save(accessToken);
    }

    public Token findByConsumerAndUser(Consumer consumer, User user) {
        return tokenDao.findByConsumerAndUser(consumer, user);
    }

    public void deleteToken(Token accessToken) {
        tokenDao.remove(accessToken.getId());
    }
}
