package com.jonwelzel.web.oauth;

import java.security.Principal;

import javax.ws.rs.core.SecurityContext;

import com.jonwelzel.commons.entities.OAuth1Consumer;
import com.jonwelzel.commons.entities.OAuth1Token;

/**
 * Security request that gets injected into the context by the OAuth filter based on the access token attached to the
 * request.
 * 
 * @author Martin Matula
 */
public class OAuth1SecurityContext implements SecurityContext {
    private final OAuth1Consumer consumer;
    private final OAuth1Token token;
    private final boolean isSecure;

    /**
     * Create a new OAuth security context from {@link OAuth1Consumer consumer}.
     * 
     * @param consumer
     *            OAuth consumer for which the context will be created.
     * @param isSecure
     *            {@code true} if the request is secured over SSL (HTTPS).
     */
    public OAuth1SecurityContext(OAuth1Consumer consumer, boolean isSecure) {
        this.consumer = consumer;
        this.token = null;
        this.isSecure = isSecure;
    }

    /**
     * Create a new OAuth security context from {@link OAuth1Token Access Token} .
     * 
     * @param token
     *            Access Token.
     * @param isSecure
     *            {@code true} if the request is secured over SSL (HTTPS).
     */
    public OAuth1SecurityContext(OAuth1Token token, boolean isSecure) {
        this.consumer = null;
        this.token = token;
        this.isSecure = isSecure;
    }

    @Override
    public Principal getUserPrincipal() {
        return consumer == null ? token.getPrincipal() : consumer.getPrincipal();
    }

    @Override
    public boolean isUserInRole(String role) {
        return consumer == null ? token.isInRole(role) : consumer.isInRole(role);
    }

    @Override
    public boolean isSecure() {
        return isSecure;
    }

    @Override
    public String getAuthenticationScheme() {
        return OAuth1Parameters.SCHEME;
    }

    public OAuth1Consumer getConsumer() {
        return consumer;
    }

    public String getToken() {
        return token != null ? token.getToken() : null;
    }
}
