package com.jonwelzel.ejb.oauth;

import java.security.NoSuchAlgorithmException;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.jonwelzel.ejb.annotations.Log;
import com.jonwelzel.ejb.consumer.ConsumerDao;
import com.jonwelzel.persistence.entities.AuthToken;
import com.jonwelzel.persistence.entities.Consumer;
import com.jonwelzel.util.SecurityUtils;

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

	public boolean isValidToken(String token) {
		log.info("Checking if token is valid");
		return tokenDao.find(token) != null;
	}

	public AuthToken find(String id) {
		return tokenDao.find(id);
	}

	/**
	 * Creates a new request token for a given consumerKey.
	 * 
	 * @param consumerKey consumer key to create a request token for
	 * @param callbackUrl callback url for this request token request
	 * @return new request token
	 */
	public AuthToken newRequestToken(String consumerKey, String callbackUrl) {
		AuthToken token = null;
		Consumer consumer = consumerDao.findByKey(consumerKey);
		if (consumer != null) {
			try {
				token = new AuthToken(SecurityUtils.generateSecureHex(), SecurityUtils.generateSecureHex(), consumer,
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
}
