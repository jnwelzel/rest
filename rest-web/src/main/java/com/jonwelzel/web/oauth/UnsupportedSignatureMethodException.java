package com.jonwelzel.web.oauth;

/**
 * Thrown to indicate that the OAuth signature method requested is not supported.
 * 
 * @author Paul C. Bryan <pbryan@sun.com>
 */
public class UnsupportedSignatureMethodException extends OAuth1SignatureException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs an unsupported OAuth method exception with no detail message.
     */
    public UnsupportedSignatureMethodException() {
        super();
    }

    /**
     * Constructs an unsupported OAuth method exception with the specified detail message.
     * 
     * @param s
     *            the detail message.
     */
    public UnsupportedSignatureMethodException(String s) {
        super(s);
    }
}
