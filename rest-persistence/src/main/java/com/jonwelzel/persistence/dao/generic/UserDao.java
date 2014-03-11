package com.jonwelzel.persistence.dao.generic;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.jonwelzel.persistence.Database;
import com.jonwelzel.persistence.entities.User;

@Stateless
@LocalBean
@Named
public class UserDao {

    @Inject
    @Database
    EntityManager entityManager;

    public List<User> findAll() {
        TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u", User.class);
        return query.getResultList();
    }
}
