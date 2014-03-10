package com.jonwelzel.persistence.dao.generic;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.jonwelzel.persistence.entities.BaseEntity;

/**
 * Implementation of a generic DAO.
 * 
 * @author jwelzel
 * 
 * @param <PK>
 *            The type od primary key that will be used.
 * @param <T>
 *            The type of the object that will be manipulated by the DAO.
 */
@Stateless
@Named("GenericDao")
@LocalBean
public class GenericDaoImpl<PK extends Serializable, T extends BaseEntity<PK>> implements GenericDao<PK, T> {

    @PersistenceContext
    private EntityManager entityManager;
    private Class<T> clazz;

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @SuppressWarnings("unchecked")
    public Class<T> getClazz() {
        if (clazz == null) {
            clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        }
        return clazz;
    }

    @Override
    public void setClazz(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T save(T t) {
        if (t.getId() != null) {
            t = update(t);
        } else {
            persist(t);
        }
        return t;
    }

    @Override
    public List<T> findAll() {
        TypedQuery<T> q = entityManager.createQuery("select e from " + clazz.getSimpleName() + " e", clazz);
        return q.getResultList();
    }

    @Override
    public T find(PK id) {
        return entityManager.getReference(clazz, id);
    }

    @Override
    public void remove(T t) {
        t = entityManager.getReference(clazz, t.getId());
        entityManager.remove(t);
    }

    /**
     * Persist new itens in database.
     * 
     * @param t
     * @return
     */
    protected T persist(T t) {
        entityManager.persist(t);
        return t;
    }

    /**
     * Update itens on database.
     * 
     * @param t
     * @return
     */
    protected T update(T t) {
        return entityManager.merge(t);
    }

}
