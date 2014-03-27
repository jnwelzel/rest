package com.jonwelzel.web.jaxrs.security;

import java.io.IOException;

import javax.annotation.ManagedBean;
import javax.annotation.Priority;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

import com.jonwelzel.ejb.oauth.AuthTokenBean;
import com.jonwelzel.ejb.session.SessionBean;
import com.jonwelzel.persistence.entities.User;

/**
 * A Servlet filter class for authorizing requests.
 * 
 * 
 * The role of this filter class is to set a {@link javax.ws.rs.core.SecurityContext} in the
 * {@link com.sun.jersey.spi.container.ContainerRequest}
 * 
 * @see {@link com.jonwelzel.web.jaxrs.security.SecurityContextImpl}
 * 
 * @author jwelzel
 */
@Provider
@Priority(Priorities.AUTHENTICATION)
@ManagedBean
public class SecurityContextFilter implements ContainerRequestFilter {

    private SessionBean sessionBean;

    private AuthTokenBean authTokenBean;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        System.out.println("SecurityContextFilter.filter()");
        final String authToken = requestContext.getHeaderString("Authorization");
        User user = null;
        if (authToken != null) {
            try {
                InitialContext initialContext = new InitialContext();
                sessionBean = (SessionBean) initialContext
                        .lookup("java:global/rest-ear-1.0-SNAPSHOT/rest-ejb-1.0-SNAPSHOT/SessionBean");
                authTokenBean = (AuthTokenBean) initialContext
                        .lookup("java:global/rest-ear-1.0-SNAPSHOT/rest-ejb-1.0-SNAPSHOT/AuthTokenBean");
                final String userId = sessionBean.getUserId(authToken);
                user = authTokenBean.find(userId).getUser();
            } catch (NamingException e) {
                System.err.println(e.getMessage());
            }
        }
        requestContext.setSecurityContext(new SecurityContextImpl(user));
    }

}
