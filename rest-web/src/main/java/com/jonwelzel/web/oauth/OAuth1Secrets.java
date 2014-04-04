package com.jonwelzel.web.oauth;


/**
 * Contains the secrets used to generate and/or verify signatures.
 * 
 * @author Paul C. Bryan <pbryan@sun.com>
 */
public class OAuth1Secrets {

    /** The consumer secret. */
    private volatile String consumerSecret;

    /** The request or access token secret. */
    private volatile String tokenSecret;

    /**
     * Returns the consumer secret.
     */
    public String getConsumerSecret() {
        return consumerSecret;
    }

    /*
     * Sets the consumer secret.
     */
    public void setConsumerSecret(String consumerSecret) {
        this.consumerSecret = consumerSecret;
    }

    /**
     * Builder pattern method to return {@link OAuth1Secrets} after setting consumer secret.
     * 
     * @param consumerSecret
     *            the consumer secret.
     */
    public OAuth1Secrets consumerSecret(String consumerSecret) {
        setConsumerSecret(consumerSecret);
        return this;
    }

    /**
     * Returns request or access token.
     */
    public String getTokenSecret() {
        return tokenSecret;
    }

    /**
     * Sets request or access token.
     */
    public void setTokenSecret(String tokenSecret) {
        this.tokenSecret = tokenSecret;
    }

    /**
     * Builder pattern method to return {@link OAuth1Secrets} after setting consumer secret.
     * 
     * @param tokenSecret
     *            the token secret.
     */
    public OAuth1Secrets tokenSecret(String tokenSecret) {
        setTokenSecret(tokenSecret);
        return this;
    }

    @Override
    public OAuth1Secrets clone() {
        return new OAuth1Secrets().consumerSecret(this.consumerSecret).tokenSecret(this.tokenSecret);
    }
}
