package com.jonwelzel.web.oauth;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;
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
public class OAuth1ServerFilter implements ContainerRequestFilter {

    @Inject
    @Log
    private Logger log;

    @Inject
    private HttpSessionBean httpSessionBean;

    @Inject
    private UserBean userBean;

    private static final String REALM = "https://localhost:8181/rest";

    /** Name of HTTP authorization header used for session auth. */
    public static final String AUTHORIZATION_HEADER = "Token";

    /** Value to return in www-authenticate header when 401 response returned. */
    private final String wwwAuthenticateHeader;

    /** OAuth protocol versions that are supported. */
    private final Set<String> versions;

    /** Manages and validates incoming nonces. */
    private final NonceManager nonces;

    public OAuth1ServerFilter() {
        // establish supported OAuth protocol versions
        HashSet<String> v = new HashSet<String>();
        v.add(null);
        v.add("1.0");
        versions = Collections.unmodifiableSet(v);
        nonces = new NonceManager();
        wwwAuthenticateHeader = "OAuth realm=\"" + REALM + "\"";
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        SecurityContext securityContext = null;
        final String authHeader = requestContext.getHeaderString(OAuth1Parameters.AUTHORIZATION_HEADER);
        if (authHeader != null && authHeader.toUpperCase().startsWith(OAuth1Parameters.SCHEME.toUpperCase())) {
            securityContext = filterOAuth(requestContext);
        } else if (authHeader != null) {
            securityContext = filterOther(requestContext);
        }
        requestContext.setSecurityContext(securityContext);
    }

    private SecurityContext filterOAuth(ContainerRequestContext requestContext) {
        return null;
    }

    private SecurityContext filterOther(ContainerRequestContext requestContext) {
        User user = null;
        final String sessionToken = requestContext.getHeaderString(AUTHORIZATION_HEADER);
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
        return new SecurityContextImpl(user);
    }

}
