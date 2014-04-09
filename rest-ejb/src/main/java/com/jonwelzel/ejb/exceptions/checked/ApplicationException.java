package com.jonwelzel.ejb.exceptions.checked;

/**
 * A checked exception thrown by the application.
 * 
 * @author jwelzel
 * 
 */
public class ApplicationException extends Exception {

    private static final long serialVersionUID = 1L;

    public ApplicationException(String message) {
        super(message);
    }

}
