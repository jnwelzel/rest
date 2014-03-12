package com.jonwelzel.persistence.dao.generic;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.jonwelzel.persistence.entities.BaseEntity;

/**
 * Abstract implementation of {@link GenericDao}. Not to be used by itself (standalone) because it does not contain an
 * {@link EntityManager} object, which is supposed to be provided by the concrete class through injection only.
 * 
 * @author jwelzel
 * 
 * @param <PK>
 *            The type od primary key that will be used.
 * @param <T>
 *            The type of the object that will be manipulated by the DAO.
 */
public abstract class AbstractGenericDao<PK extends Serializable, T extends BaseEntity<PK>> implements
        GenericDao<PK, T> {

    private Class<T> clazz;

    public abstract EntityManager getEntityManager();

    @SuppressWarnings("unchecked")
    public Class<T> getEntityClass() {
        if (clazz == null) {
            clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
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
        TypedQuery<T> q = getEntityManager().createQuery("select e from " + getEntityClass().getSimpleName() + " e",
                clazz);
        return q.getResultList();
    }

    @Override
    public T find(PK id) {
        return id != null ? getEntityManager().find(clazz, id) : null;
    }

    @Override
    public void remove(T t) {
        t = getEntityManager().getReference(clazz, t.getId());
        getEntityManager().remove(t);
    }

    /**
     * Persist new items in database.
     * 
     * @param t
     * @return
     */
    protected T persist(T t) {
        getEntityManager().persist(t);
        return t;
    }

    /**
     * Update items on database.
     * 
     * @param t
     * @return
     */
    protected T update(T t) {
        return getEntityManager().merge(t);
    }

}
