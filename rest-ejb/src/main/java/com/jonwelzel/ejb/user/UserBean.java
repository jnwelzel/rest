package com.jonwelzel.ejb.user;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;

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

    @EJB
    private UserDao userDao;

    public List<User> findAll() {
        log.info("Finding all users");
        return userDao.findAll();
    }

    public User findUser(Long id) {
        log.info("Finding user with \"id\" " + id);
        return userDao.find(id);
    }

    public User createUser(User user) {
        log.info("Creating a new user");
        return userDao.save(user);
    }

    public User updateUser(User user) {
        log.info("Updating user \"" + user.getName() + "\"");
        return userDao.save(user);
    }
}
