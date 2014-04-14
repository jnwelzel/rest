package com.jonwelzel.ejb.user;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.jonwelzel.commons.dao.AbstractJpaDao;
import com.jonwelzel.commons.entities.User;

/**
 * DAO for {@link User}.
 * 
 * @author jwelzel
 * 
 */
@Stateless
public class UserDao extends AbstractJpaDao<Long, User> {

    @PersistenceContext(unitName = "rest")
    private EntityManager entityManager;

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    public User findByEmail(String email) {
        TypedQuery<User> query = getEntityManager().createNamedQuery("User.findByEmail", User.class);
        query.setParameter("email", email);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public User findByToken(String authToken) {
        TypedQuery<User> query = getEntityManager().createNamedQuery("User.findByToken", User.class);
        query.setParameter("authToken", authToken);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public User findByAlias(String alias) {
        TypedQuery<User> query = getEntityManager().createNamedQuery("User.findByAlias", User.class);
        query.setParameter("alias", alias);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}
