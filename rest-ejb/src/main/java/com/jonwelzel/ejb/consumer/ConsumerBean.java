package com.jonwelzel.ejb.consumer;

import java.security.NoSuchAlgorithmException;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.jonwelzel.ejb.annotations.Log;
import com.jonwelzel.persistence.entities.Consumer;
import com.jonwelzel.util.SecurityUtils;

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
        return consumerDao.save(consumer);
    }

    public Consumer findConsumer(Long id) {
        return consumerDao.find(id);
    }

    public Consumer findByKey(String consumerKey) {
        return consumerDao.findByKey(consumerKey);
    }

}
