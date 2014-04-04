package com.jonwelzel.web.oauth;

/**
 * An OAuth signature method that implements Plaintext.
 * 
 * @author Paul C. Bryan <pbryan@sun.com>
 */
public final class PlaintextMethod implements OAuth1SignatureMethod {

    /**
     * Method name.
     */
    public static final String NAME = "PLAINTEXT";

    @Override
    public String name() {
        return NAME;
    }

    /**
     * Generates the PLAINTEXT signature.
     * 
     * @param baseString
     *            the OAuth elements to sign (ignored).
     * @param secrets
     *            the shared secrets used to sign the request.
     * @return the plaintext OAuth signature.
     */
    @Override
    public String sign(String baseString, OAuth1Secrets secrets) {
        StringBuffer buf = new StringBuffer();
        String secret = secrets.getConsumerSecret();
        if (secret != null) {
            buf.append(secret);
        }
        buf.append('&');
        secret = secrets.getTokenSecret();
        if (secret != null) {
            buf.append(secret);
        }
        return buf.toString();
    }

    /**
     * Verifies the Plaintext signature.
     * 
     * @param elements
     *            OAuth elements (ignored).
     * @param secrets
     *            the shared secrets for verifying the signature.
     * @param signature
     *            plaintext OAuth signature to be verified.
     */
    @Override
    public boolean verify(String elements, OAuth1Secrets secrets, String signature) {
        return sign(elements, secrets).equals(signature);
    }
}
