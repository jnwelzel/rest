package com.jonwelzel.web.jaxrs.security;

import java.security.Principal;

import com.jonwelzel.persistence.entities.User;

/**
 * Class that manages the security context.
 * 
 * @author jwelzel
 * 
 */
public class SecurityContext implements javax.ws.rs.core.SecurityContext {

    private final User user;

    public SecurityContext(User user) {
        this.user = user;
    }

    @Override
    public String getAuthenticationScheme() {
        return SecurityContext.FORM_AUTH;
    }

    @Override
    public Principal getUserPrincipal() {
        return user;
    }

    @Override
    public boolean isSecure() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isUserInRole(String role) {
        return user.getRoles().contains(role);
    }

}
