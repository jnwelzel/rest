package com.jonwelzel.ejb.consumer;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.jonwelzel.commons.entities.Consumer;
import com.jonwelzel.commons.enumerations.RoleType;
import com.jonwelzel.commons.utils.SecurityUtils;
import com.jonwelzel.ejb.annotations.Log;

@Stateless
public class ConsumerBean {

    @Inject
    @Log
    private Logger log;

    @Inject
    private ConsumerDao consumerDao;

    public Consumer createConsumer(Consumer consumer) throws NoSuchAlgorithmException {
        consumer.setSecret(SecurityUtils.generateSecureHex());
        consumer.setKey(SecurityUtils.generateSecureHex());
        consumer.setRoles(Arrays.asList(RoleType.CONSUMER));
        return consumerDao.save(consumer);
    }

    public Consumer findConsumer(Long id) {
        return consumerDao.find(id);
    }

    public Consumer findByKey(String consumerKey) {
        return consumerDao.findByKey(consumerKey);
    }

    /**
     * Find a Consumer by an oAuth token.
     * 
     * @param authToken
     *            The token that's automatically generated for this user.
     * @return The Consumer or null.
     */
    public Consumer findByToken(String oauthToken) {
        return consumerDao.findByToken(oauthToken);
    }
}
