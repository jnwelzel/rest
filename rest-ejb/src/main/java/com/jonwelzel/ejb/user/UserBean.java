package com.jonwelzel.ejb.user;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.jonwelzel.ejb.annotations.Log;
import com.jonwelzel.persistence.entities.AuthToken;
import com.jonwelzel.persistence.entities.User;
import com.jonwelzel.util.SecurityUtils;

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
    @Log
    private Logger log;

    @EJB
    private UserDao userDao;

    public List<User> findAll() {
        log.info("Finding all users");
        return userDao.findAll();
    }

    public User findUser(Long id) {
        log.info("Finding user with id \"" + id + "\"");
        return userDao.find(id);
    }

    public User createUser(User user) {
        log.info("Creating a new user");
        AuthToken token = new AuthToken();
        try {
            token.setId(SecurityUtils.generateSecureHex());
            token.setUser(user);
            user.getAuthTokens().add(token);
            user.setPasswordHash(SecurityUtils.createHash(user.getPassword()));
            user.setPassword(null);
        } catch (NoSuchAlgorithmException e) {
            log.error("The \"SHA1\" encryption algorithm needed for the user's authentication token creation could not be found.");
        } catch (InvalidKeySpecException e) {
            log.error("Could not generate user password hash because the provided \"PBKDF2WithHmacSHA1\" key specification is invalid.");
        }
        return userDao.save(user);
    }

    public User updateUser(User user) {
        log.info("Updating user \"" + user.getName() + "\"");
        return userDao.save(user);
    }

    public void deleteUser(Long id) {
        log.info("Deleting user with id \"" + id + "\"");
        userDao.remove(id);
    }

    /**
     * Find a {@link User} by its registered email address.
     * 
     * @param email
     *            The user's email.
     * @return The {@link User} object if found or null if no record was found.
     */
    public User findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    /**
     * Find a {@link User} by its authorization token.
     * 
     * @param authToken
     *            The token that's automatically generated for this user.
     * @return The user or null.
     */
    public User findByToken(AuthToken authToken) {
        return userDao.findByToken(authToken);
    }

}
