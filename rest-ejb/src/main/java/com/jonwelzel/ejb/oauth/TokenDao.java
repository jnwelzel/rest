package com.jonwelzel.ejb.oauth;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.jonwelzel.commons.dao.AbstractJpaDao;
import com.jonwelzel.commons.entities.Token;

@Stateless
public class TokenDao extends AbstractJpaDao<Long, Token> {

    @PersistenceContext(unitName = "rest")
    private EntityManager entityManager;

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    public Token findByToken(String token) {
        TypedQuery<Token> q = getEntityManager().createNamedQuery("Token.findByToken", Token.class);
        q.setParameter("token", token);
        try {
            return q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Token findByVerifier(String verifier) {
        TypedQuery<Token> q = getEntityManager().createNamedQuery("Token.findByVerifier", Token.class);
        q.setParameter("verifier", verifier);
        try {
            return q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}
