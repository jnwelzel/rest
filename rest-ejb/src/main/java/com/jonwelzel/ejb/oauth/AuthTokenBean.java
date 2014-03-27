package com.jonwelzel.ejb.oauth;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.jonwelzel.persistence.entities.AuthToken;

@Stateless(mappedName = "AuthTokenBean")
@LocalBean
public class AuthTokenBean {

    @Inject
    private Logger log;

    @EJB
    private AuthTokenDao tokenDao;

    public boolean isValidToken(String token) {
        log.info("Checking if token is valid");
        return tokenDao.find(token) != null;
    }

    public AuthToken find(String id) {
        return tokenDao.find(id);
    }
}
