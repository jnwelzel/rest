package com.jonwelzel.ejb.user;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.jonwelzel.persistence.dao.user.UserDao;
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
    private Logger log;

    @Inject
    private UserDao userDao;

    public List<User> findAll() {
        log.info("Finding all users");
        userDao.findAll();
        return new ArrayList<User>();
    }

    public User findUser(Long id) {
        log.info("Finding user with \"id\" " + id);
        // TODO Auto-generated method stub
        return null;
    }

    public User createUser(User user) {
        log.info("Creating a new user");
        return userDao.save(user);
    }
}
