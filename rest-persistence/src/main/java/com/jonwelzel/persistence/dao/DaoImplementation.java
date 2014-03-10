package com.jonwelzel.persistence.dao;

import java.io.Serializable;

import com.jonwelzel.persistence.dao.generic.GenericDao;
import com.jonwelzel.persistence.entities.BaseEntity;

/**
 * Interface that defines a {@link GenericDao} implementation.
 * 
 * @author jwelzel
 * 
 * @param <PK>
 *            Primary key type.
 * @param <T>
 *            Underlying class type.
 */
public interface DaoImplementation<PK extends Serializable, T extends BaseEntity<PK>> {

    public GenericDao<PK, T> getGenericDao();

}
