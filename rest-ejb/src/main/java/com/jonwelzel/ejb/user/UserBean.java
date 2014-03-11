package com.jonwelzel.ejb.user;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.jonwelzel.persistence.dao.generic.UserDao;
import com.jonwelzel.persistence.entities.User;

/**
 * {@link User} EJB.
 * 
 * @author jwelzel
 * 
 */
@Stateless(mappedName = "UserBean")
@LocalBean
public class UserBean {

    @Inject
    Logger log;

    @Inject
    UserDao userDao;

    public List<User> findAll() {
        log.info("Finding all users");
        userDao.findAll();
        // TODO Auto-generated method stub
        return new ArrayList<User>();
    }

    public User findUser(Long id) {
        log.info("Finding user with \"id\" " + id);
        // TODO Auto-generated method stub
        return null;
    }

}
