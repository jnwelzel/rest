package com.jonwelzel.persistence.dao.user;

import javax.inject.Inject;

import com.jonwelzel.persistence.dao.DaoImplementation;
import com.jonwelzel.persistence.dao.generic.Dao;
import com.jonwelzel.persistence.dao.generic.GenericDao;
import com.jonwelzel.persistence.dao.generic.GenericDaoImpl;
import com.jonwelzel.persistence.entities.User;

/**
 * DAO for {@link User}.
 * 
 * @author jwelzel
 * 
 */
public class UserDao implements DaoImplementation<Long, User> {

    @Inject
    @Dao
    private GenericDaoImpl<Long, User> genericDao;

    @Override
    public GenericDao<Long, User> getGenericDao() {
        return genericDao;
    }

}
