package com.jonwelzel.web.oauth;

/**
 * Thrown to indicate that the OAuth secret supplied is invalid or otherwise unsupported.
 * 
 * @author Paul C. Bryan <pbryan@sun.com>
 */
public class InvalidSecretException extends OAuth1SignatureException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs an invalid OAuth secret exception with no detail message.
     */
    public InvalidSecretException() {
        super();
    }

    /**
     * Constructs an invalid OAuth secret exception with the specified detail message.
     * 
     * @param s
     *            the detail message.
     */
    public InvalidSecretException(String s) {
        super(s);
    }
}
