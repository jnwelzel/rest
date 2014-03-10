package com.jonwelzel.web;

import java.io.Serializable;
import java.util.List;

import javax.ws.rs.PathParam;

import com.jonwelzel.persistence.entities.Bean;

/**
 * Base interface defining core methods for resources.
 * 
 * @author jwelzel
 * 
 * @param <PK>
 *            Type of the resource primary key.
 * @param <T>
 *            Type of the resource.
 */
public interface Resource<PK extends Serializable, T extends Bean<PK>> {

    public List<T> getResources();

    public T getResource(@PathParam("id") PK id);

}
