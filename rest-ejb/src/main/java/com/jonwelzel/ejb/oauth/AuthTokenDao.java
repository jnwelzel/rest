package com.jonwelzel.ejb.oauth;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.jonwelzel.commons.dao.AbstractJpaDao;
import com.jonwelzel.commons.entities.Token;

@Stateless
public class AuthTokenDao extends AbstractJpaDao<String, Token> {

    @PersistenceContext(unitName = "rest")
    private EntityManager entityManager;

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

}
