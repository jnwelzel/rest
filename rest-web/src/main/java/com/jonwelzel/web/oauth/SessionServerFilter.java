package com.jonwelzel.web.oauth;

import java.io.IOException;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;

import com.jonwelzel.commons.entities.User;
import com.jonwelzel.ejb.annotations.Log;
import com.jonwelzel.ejb.session.HttpSessionBean;
import com.jonwelzel.ejb.user.UserBean;
import com.jonwelzel.web.security.SecurityContextImpl;

/**
 * A filter class for authenticating requests using application user data if available.
 * 
 * 
 * The role of this filter class is to set a {@link javax.ws.rs.core.SecurityContext} in the
 * {@link ContainerRequestFilter}
 * 
 * @see {@link com.jonwelzel.web.security.SecurityContextImpl}
 * 
 * @author jwelzel
 */
@Provider
@Priority(1500)
public class SessionServerFilter implements ContainerRequestFilter {

    @Inject
    @Log
    private Logger log;

    @Inject
    private HttpSessionBean httpSessionBean;

    @Inject
    private UserBean userBean;

    public static final String IDENTITY_SESSION_HEADER = "Identity-Session";

    @Override
    public void filter(ContainerRequestContext request) throws IOException {
        if (request.getSecurityContext().getUserPrincipal() != null) {
            return;
        }
        final String authHeader = request.getHeaderString(IDENTITY_SESSION_HEADER);
        User user = null;
        if (authHeader != null) {
            final String userId = httpSessionBean.getUserId(authHeader);
            if (userId == null) {
                // Ops, session expired buddy
                request.abortWith(Response.status(Status.UNAUTHORIZED).entity("Session expired!").build());
            } else {
                user = userBean.findUser(Long.valueOf(userId));
            }
        }
        log.info("[" + request.getRequest().getMethod() + "] \"" + request.getUriInfo().getPath() + "\" (User=" + user
                + ", token=" + authHeader + ")");
        request.setSecurityContext(new SecurityContextImpl(user));
    }

}
