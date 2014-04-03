package com.jonwelzel.web.providers;

import java.io.IOException;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;

import com.jonwelzel.ejb.annotations.Log;
import com.jonwelzel.ejb.session.HttpSessionBean;
import com.jonwelzel.ejb.user.UserBean;
import com.jonwelzel.persistence.entities.User;
import com.jonwelzel.web.security.SecurityContextImpl;

/**
 * A filter class for authenticating requests.
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
@Priority(Priorities.AUTHENTICATION)
public class SecurityFilter implements ContainerRequestFilter {

    @Inject
    @Log
    private Logger log;

    @Inject
    private HttpSessionBean httpSessionBean;

    @Inject
    private UserBean userBean;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        final String sessionToken = requestContext.getHeaderString("Authorization");
        User user = null;
        if (sessionToken != null) {
            final String userId = httpSessionBean.getUserId(sessionToken);
            if (userId == null) {
                // Ops, session expired buddy
                requestContext.abortWith(Response.status(Status.UNAUTHORIZED).entity("Session expired!").build());
            } else {
                user = userBean.findUser(Long.valueOf(userId));
            }
        }
        log.info("[" + requestContext.getRequest().getMethod() + "] \"" + requestContext.getUriInfo().getPath()
                + "\" (User=" + user + ", token=" + sessionToken + ")");
        requestContext.setSecurityContext(new SecurityContextImpl(user));
    }

}
