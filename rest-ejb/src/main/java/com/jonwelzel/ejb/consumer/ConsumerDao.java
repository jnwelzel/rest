package com.jonwelzel.ejb.consumer;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.jonwelzel.persistence.dao.generic.AbstractJpaDao;
import com.jonwelzel.persistence.entities.Consumer;

public class ConsumerDao extends AbstractJpaDao<Long, Consumer> {

    @PersistenceContext(unitName = "rest")
    private EntityManager entityManager;

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    public Consumer findByKey(String key) {
        TypedQuery<Consumer> query = getEntityManager().createNamedQuery("Consumer.findByKey", Consumer.class);
        query.setParameter("key", key);
        Consumer result = null;
        try {
            result = query.getSingleResult();
        } catch (NoResultException e) {
        }
        return result;
    }

}
