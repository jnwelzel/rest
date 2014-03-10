package com.jonwelzel.persistence.dao.generic;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionPoint;

import com.jonwelzel.persistence.entities.BaseEntity;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class DaoProducer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Produces
    @Dependent
    @Dao
    public <ID extends Serializable, T extends BaseEntity<ID>> GenericDao<ID, T> produce(InjectionPoint ip,
            BeanManager bm) {
        if (ip.getAnnotated().isAnnotationPresent(Dao.class)) {
            GenericDao<ID, T> genericDao = (GenericDao<ID, T>) getBeanByName("GenericDao", bm);
            ParameterizedType type = (ParameterizedType) ip.getType();
            Type[] typeArgs = type.getActualTypeArguments();
            Class<T> entityClass = (Class<T>) typeArgs[0];
            genericDao.setClazz(entityClass);
            return genericDao;
        }
        throw new IllegalArgumentException("The \"@Dao\" annotation is required when injecting a \"GenericDao\".");
    }

    public Object getBeanByName(String name, BeanManager bm) { // eg. name=availableCountryDao{
        Bean bean = bm.getBeans(name).iterator().next();
        CreationalContext ctx = bm.createCreationalContext(bean); // could be inlined below
        Object o = bm.getReference(bean, bean.getBeanClass(), ctx); // could be inlined with return
        return o;
    }
}
