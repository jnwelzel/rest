package com.jonwelzel.ejb.oauth;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.jonwelzel.persistence.dao.generic.AbstractJpaDao;
import com.jonwelzel.persistence.entities.AuthToken;

@Stateless
public class AuthTokenDao extends AbstractJpaDao<String, AuthToken> {

    @PersistenceContext(unitName = "rest")
    private EntityManager entityManager;

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

}
