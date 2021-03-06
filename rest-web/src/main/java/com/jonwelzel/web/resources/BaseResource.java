package com.jonwelzel.web.resources;

import java.io.Serializable;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.SecurityContext;

import com.jonwelzel.commons.entities.AbstractEntity;

/**
 * Base interface that defines core methods for persistent resources.
 * 
 * @author jwelzel
 * 
 * @param <PK>
 *            Type of the resource primary key.
 * @param <T>
 *            Type of the resource.
 */
public interface BaseResource<PK extends Serializable, T extends AbstractEntity<PK>> {

    /**
     * Retrieve all records of the {@linkplain T} resource type.
     * 
     * @return A list containing all records found or an empty list if none were found.
     */
    public List<T> getResources();

    /**
     * Retrieve a single record of the {@linkplain T} resource type by its identification attribute.
     * 
     * @param id
     *            The resource id type.
     * @return A single record if found with id, or null if none found.
     */
    public T getResource(PK id);

    /**
     * Create a new record of the {@linkplain T} resource type.
     * 
     * @param resouce
     *            The object containing the resource data that will be created.
     * @return The freshly saved resource object.
     */
    public T createResource(T resource, SecurityContext securityContext) throws WebApplicationException;

    /**
     * Update the existing record of this resource.
     * 
     * @param resouce
     *            The object containing the data that will be updated.
     * @return The freshly updated resource object.
     */
    public T updateResource(T resource);

    /**
     * Delete the resource from the database.
     * 
     * @param id
     *            The id of the record to be deleted.
     */
    public void deleteResource(PK id);

}
