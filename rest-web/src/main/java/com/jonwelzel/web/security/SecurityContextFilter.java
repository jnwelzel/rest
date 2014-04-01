package com.jonwelzel.web.security;

import java.io.IOException;

import javax.annotation.Priority;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import com.jonwelzel.ejb.oauth.AuthTokenBean;
import com.jonwelzel.ejb.session.SessionBean;
import com.jonwelzel.ejb.user.UserBean;
import com.jonwelzel.persistence.entities.AuthToken;
import com.jonwelzel.persistence.entities.User;
import com.jonwelzel.util.Version;

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
public class SecurityContextFilter implements ContainerRequestFilter {

    private SessionBean sessionBean;

    private AuthTokenBean authTokenBean;

    private UserBean userBean;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        System.out.println("SecurityContextFilter.filter()");
        final String sessionToken = requestContext.getHeaderString("Authorization");
        User user = null;
        if (sessionToken != null) {
            try {
                // FUUUUUUUUUUCK! JAX-RS can't use DI here (https://java.net/jira/browse/GLASSFISH-20534)
                InitialContext initialContext = new InitialContext();
                sessionBean = (SessionBean) initialContext.lookup("java:global/rest-ear-" + Version.VALUE
                        + "/rest-ejb/SessionBean");
                final String userId = sessionBean.getUserId(sessionToken);
                if (userId == null) {
                    // Ops, session expired buddy
                    requestContext.abortWith(Response.status(Status.UNAUTHORIZED).entity("Session expired!").build());
                } else {
                    authTokenBean = (AuthTokenBean) initialContext.lookup("java:global/rest-ear-" + Version.VALUE
                            + "/rest-ejb/AuthTokenBean");
                    userBean = (UserBean) initialContext.lookup("java:global/rest-ear-" + Version.VALUE
                            + "/rest-ejb/UserBean");
                    final AuthToken authToken = authTokenBean.find(userId);
                    user = userBean.findByToken(authToken);
                }
            } catch (NamingException e) {
                System.err.println(e.getMessage());
            }
        }
        requestContext.setSecurityContext(new SecurityContextImpl(user));
    }

}
