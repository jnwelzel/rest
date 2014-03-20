package com.jonwelzel.ejb.user;

import java.security.NoSuchAlgorithmException;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;

import com.jonwelzel.persistence.dao.generic.AbstractGenericDao;
import com.jonwelzel.persistence.entities.AuthToken;
import com.jonwelzel.persistence.entities.User;
import com.jonwelzel.util.Security;

/**
 * DAO for {@link User}.
 * 
 * @author jwelzel
 * 
 */
@Stateless
public class UserDao extends AbstractGenericDao<Long, User> {

    @Inject
    private Logger log;

    @PersistenceContext(unitName = "rest")
    private EntityManager entityManager;

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    protected User persist(User t) {
        AuthToken token = new AuthToken();
        try {
            token.setId(Security.generateSecureHex());
            token.setUser(t);
        } catch (NoSuchAlgorithmException e) {
            log.error("The \"SHA1\" encryption algorithm needed for the user's authentication token creation could not be found.");
        }
        t.setAuthToken(token);
        return super.persist(t);
    }

}
