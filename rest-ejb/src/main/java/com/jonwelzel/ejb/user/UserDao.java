package com.jonwelzel.ejb.user;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.jonwelzel.commons.dao.AbstractJpaDao;
import com.jonwelzel.commons.entities.Token;
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
        User dbUser = null;
        try {
            dbUser = query.getSingleResult();
        } catch (NoResultException e) {
        }
        return dbUser;
    }

    public User findByToken(Token authToken) {
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
