package com.jonwelzel.ejb.user;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

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

    public List<User> findAll() {
        System.out.println("Finding all users");
        // TODO Auto-generated method stub
        return new ArrayList<User>();
    }

    public User findUser(Long id) {
        System.out.println("Finding user with \"id\" " + id);
        // TODO Auto-generated method stub
        return null;
    }

}
