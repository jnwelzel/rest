package com.jonwelzel.web.oauth;

import javax.inject.Singleton;

/**
 * An interface representing the OAuth signature method.
 * 
 * @author Hubert A. Le Van Gong <hubert.levangong at Sun.COM>
 * @author Paul C. Bryan <pbryan@sun.com>
 */
@Singleton
public interface OAuth1SignatureMethod {

    /**
     * Returns the name of this signature method, as negotiated through the OAuth protocol.
     * 
     * @return Signature method name.
     */
    public String name();

    /**
     * Signs the data using the supplied secret(s).
     * 
     * @param baseString
     *            a {@link String} that contains the request baseString to be signed.
     * @param secrets
     *            the secret(s) to use to sign the data.
     * @return a {@link String} that contains the signature.
     * @throws InvalidSecretException
     *             if a supplied secret is not valid.
     */
    public String sign(String baseString, OAuth1Secrets secrets) throws InvalidSecretException;

    /**
     * Verifies the signature for the data using the supplied secret(s).
     * 
     * @param elements
     *            a {@link String} that contains the request elements to be verified.
     * @param secrets
     *            the secret(s) to use to verify the signature.
     * @param signature
     *            a {@link String} that contains the signature to be verified.
     * @return true if the signature matches the secrets and data.
     * @throws InvalidSecretException
     *             if a supplied secret is not valid.
     */
    public boolean verify(String elements, OAuth1Secrets secrets, String signature) throws InvalidSecretException;
}