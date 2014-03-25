package com.jonwelzel.web.validation;

import javax.ws.rs.WebApplicationException;

/**
 * Validation strategy necessary to verify if request should be granted access to application resources.
 * 
 * @author jwelzel
 * 
 */
public interface RequestAuthValidator {

    /**
     * Logic used to verify if the request is authorized to access the annotated resource.
     * 
     * @return
     * @throws WebApplicationException
     */
    public boolean validate() throws WebApplicationException;

}
