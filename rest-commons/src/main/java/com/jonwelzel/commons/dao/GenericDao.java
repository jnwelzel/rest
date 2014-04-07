package com.jonwelzel.commons.dao;

import java.io.Serializable;
import java.util.List;

import com.jonwelzel.commons.entities.AbstractEntity;

/**
 * DAO interface that defines the basic CRUD operations.
 * 
 * @author jwelzel
 * 
 * @param <PK>
 *            The type od primary key that will be used.
 * @param <T>
 *            The type of the object that will be manipulated by the DAO.
 */
public interface GenericDao<PK extends Serializable, T extends AbstractEntity<PK>> {

    /**
     * Persist the given entity using the EntityManager.
     * 
     * @param t
     *            entity to be saved.
     */
    public T save(T t);

    /**
     * Find all items of this type in the database.
     * 
     * @return a List of T elements from database.
     */
    public List<T> findAll();

    /**
     * Find an item from database based on its ID.
     * 
     * @param id
     *            to look for.
     * @return found entity or null if no entity is found.
     */
    public T find(PK id);

    /**
     * Delete the item from database.
     * 
     * @param t
     *            item to delete.
     */
    public void remove(PK id);

    /**
     * Count all.
     * 
     * @return Value indicating the number of records the table currently holds.
     */
    public Long count();

    public void setClazz(Class<T> clazz);

}
