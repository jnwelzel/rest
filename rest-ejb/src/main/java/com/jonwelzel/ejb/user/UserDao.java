package com.jonwelzel.ejb.user;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;

import com.jonwelzel.persistence.dao.generic.AbstractJpaDao;
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
public class UserDao extends AbstractJpaDao<Long, User> {

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
            t.setAuthToken(token);
            t.setPasswordHash(Security.createHash(t.getPassword()));
        } catch (NoSuchAlgorithmException e) {
            log.error("The \"SHA1\" encryption algorithm needed for the user's authentication token creation could not be found.");
        } catch (InvalidKeySpecException e) {
            log.error("Could not generate user password hash because the provided \"PBKDF2WithHmacSHA1\" key specification is invalid.");
        }
        return super.persist(t);
    }

}
