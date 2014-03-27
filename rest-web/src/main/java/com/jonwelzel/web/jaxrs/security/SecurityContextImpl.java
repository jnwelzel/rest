package com.jonwelzel.web.jaxrs.security;

import java.security.Principal;

import com.jonwelzel.persistence.entities.User;

/**
 * Class that manages the security context.
 * 
 * @author jwelzel
 * 
 */
public class SecurityContextImpl implements javax.ws.rs.core.SecurityContext {

    private final User user;

    public SecurityContextImpl(User user) {
        this.user = user;
    }

    @Override
    public String getAuthenticationScheme() {
        return SecurityContextImpl.BASIC_AUTH;
    }

    @Override
    public Principal getUserPrincipal() {
        return user;
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public boolean isUserInRole(String role) {
        return user == null ? false : user.getRoles().contains(role);
    }

}
