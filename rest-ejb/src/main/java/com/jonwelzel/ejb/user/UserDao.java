package com.jonwelzel.ejb.user;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.jonwelzel.persistence.dao.generic.AbstractGenericDao;
import com.jonwelzel.persistence.entities.User;

/**
 * DAO for {@link User}.
 * 
 * @author jwelzel
 * 
 */
@Stateless
public class UserDao extends AbstractGenericDao<Long, User> {

    @PersistenceContext(unitName = "rest")
    private EntityManager entityManager;

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

}
