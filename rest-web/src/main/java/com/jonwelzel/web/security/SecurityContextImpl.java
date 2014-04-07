package com.jonwelzel.web.security;

import java.security.Principal;

import javax.ws.rs.core.SecurityContext;

import com.jonwelzel.commons.entities.User;
import com.jonwelzel.commons.enumerations.RoleType;

/**
 * Class that manages the security context.
 * 
 * @author jwelzel
 * 
 */
public class SecurityContextImpl implements SecurityContext {

    private final User user;

    public SecurityContextImpl(User user) {
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
        return false;
    }

    @Override
    public boolean isUserInRole(String role) {
        return user == null ? false : user.getRoles().contains(RoleType.valueOf(role));
    }

}
