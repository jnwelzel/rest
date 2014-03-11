package com.jonwelzel.persistence.dao.user;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import com.jonwelzel.persistence.Database;
import com.jonwelzel.persistence.dao.generic.AbstractGenericDao;
import com.jonwelzel.persistence.entities.User;

/**
 * DAO for {@link User}.
 * 
 * @author jwelzel
 * 
 */
@Stateless
@LocalBean
@Named
public class UserDao extends AbstractGenericDao<Long, User> {

    @Inject
    @Database
    private EntityManager entityManager;

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

}
