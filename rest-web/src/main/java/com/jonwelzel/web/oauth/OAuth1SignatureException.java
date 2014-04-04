package com.jonwelzel.web.oauth;

/**
 * Thrown to indicate that an OAuth exception occurred.
 * 
 * @author Paul C. Bryan <pbryan@sun.com>
 */
public class OAuth1SignatureException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs an OAuth signature exception with no detail message.
     */
    public OAuth1SignatureException() {
        super();
    }

    /**
     * Constructs an OAuth signature exception with the specified detail message.
     * 
     * @param s
     *            the detail message.
     */
    public OAuth1SignatureException(String s) {
        super(s);
    }

    /**
     * Constructs an OAuth signature exception with the specified cause.
     * 
     * @param cause
     *            the cause of the exception.
     */
    public OAuth1SignatureException(Throwable cause) {
        super(cause);
    }
}
