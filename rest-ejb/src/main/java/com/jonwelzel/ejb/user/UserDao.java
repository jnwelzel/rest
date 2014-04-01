package com.jonwelzel.ejb.user;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;

import com.jonwelzel.ejb.annotations.Log;
import com.jonwelzel.persistence.dao.generic.AbstractJpaDao;
import com.jonwelzel.persistence.entities.AuthToken;
import com.jonwelzel.persistence.entities.User;

/**
 * DAO for {@link User}.
 * 
 * @author jwelzel
 * 
 */
@Stateless
public class UserDao extends AbstractJpaDao<Long, User> {

    @Inject
    @Log
    private Logger log;

    @PersistenceContext(unitName = "rest")
    private EntityManager entityManager;

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    public User findByEmail(String email) {
        TypedQuery<User> query = getEntityManager().createNamedQuery("User.findByEmail", User.class);
        query.setParameter("email", email);
        User dbUser = null;
        try {
            dbUser = query.getSingleResult();
        } catch (NoResultException e) {
        }
        return dbUser;
    }

    public User findByToken(AuthToken authToken) {
        TypedQuery<User> query = getEntityManager().createNamedQuery("User.findByToken", User.class);
        query.setParameter("authToken", authToken);
        User dbUser = null;
        try {
            dbUser = query.getSingleResult();
        } catch (NoResultException e) {
        }
        return dbUser;
    }

}
