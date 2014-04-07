package com.jonwelzel.commons.entities;

import java.security.Principal;
import java.util.Map;

/**
 * Interface representing an OAuth token (i.e. access token or request token).
 * 
 * @author Martin Matula
 */
public interface OAuth1Token {
    /**
     * Returns string representing the token.
     * 
     * @return string representing the token
     */
    String getToken();

    /**
     * Returns the token secret.
     * 
     * @return token secret
     */
    String getSecret();

    /**
     * Returns consumer this token was issued for.
     * 
     * @return consumer this token was issued for.
     */
    OAuth1Consumer getConsumer();

    /**
     * Returns additional custom attributes associated with the token. If this is a request token, this should be a the
     * same set or a defined subset of parameters that were passed to the
     * {@link OAuth1Provider#newRequestToken(String, String, java.util.Map)} method that created this request token. If
     * this is an access token, this is any application defined set that will included as form parameters in a response
     * to accessToken request.
     * 
     * @return immutable map of custom attributes
     */
    Map<String, String> getAttributes();

    /**
     * Returns a {@link java.security.Principal} object containing the name of the user the request containing this
     * token is authorized to act on behalf of. When the oauth filter verifies the request with this token is properly
     * authenticated, it injects this token into a security context which then delegates
     * {@link javax.ws.rs.core.SecurityContext#getUserPrincipal()} to this method.
     * 
     * @return Principal corresponding to this token, or null if the token is not authorized
     */
    Principal getPrincipal();

    /**
     * Returns a boolean indicating whether this token is authorized for the specified logical "role". When the oauth
     * filter verifies the request with this token is properly authenticated, it injects this token into a security
     * context which then delegates {@link javax.ws.rs.core.SecurityContext#isUserInRole(String)} to this method.
     * 
     * @param role
     *            a {@code String} specifying the name of the role
     * 
     * @return a {@code boolean} indicating whether this token is authorized for a given role
     */
    boolean isInRole(String role);
}
