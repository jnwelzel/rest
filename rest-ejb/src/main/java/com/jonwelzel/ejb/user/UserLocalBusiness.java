package com.jonwelzel.ejb.user;

import java.util.List;

import javax.ejb.Local;

import com.jonwelzel.persistence.entities.User;

/**
 * Business definitions for {@link User}.
 * 
 * @author jwelzel
 * 
 */
@Local
public interface UserLocalBusiness {

    public List<User> findAll();

    public User findUser(Long id);

}
