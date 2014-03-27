package com.jonwelzel.web.resources.session;

import java.security.NoSuchAlgorithmException;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jonwelzel.ejb.session.SessionBean;
import com.jonwelzel.ejb.user.UserBean;
import com.jonwelzel.persistence.entities.User;
import com.jonwelzel.util.SecurityUtils;

public class SessionResourceImpl implements SessionResource {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Inject
    private UserBean userBean;

    @Inject
    private SessionBean sessionBean;

    @Override
    public Response login(User user) {
        log.info("Log in for user \"" + user.getEmail() + "\"");
        User dbUser = userBean.findByEmail(user.getEmail());
        if (dbUser == null) {
            throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity("Invalid email address.")
                    .build());
        }
        if (!SecurityUtils.validatePassword(user.getPassword(), dbUser.getPasswordHash())) {
            throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity("Invalid password.").build());
        }
        try { // Now the session stuff
            String hash = SecurityUtils.generateSecureHex();
            sessionBean.newSession(hash, dbUser.getAuthToken().getId(), false);
            dbUser.getAuthToken().setId(hash); // send back the session secret and not the user id ;)
        } catch (NoSuchAlgorithmException e) {
            log.error("The \"SHA1\" encryption algorithm needed to generate the user session hash could not be found.");
            throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity("Could not secure user session.").build());
        }
        return Response.status(Status.OK).entity(dbUser).build();
    }

    @Override
    public Response logout(String sessionToken) {
        int result = sessionBean.destroySession(sessionToken);
        Response response = null;
        if (result == 1 || result == 0) {
            response = Response.status(Status.OK).build();
        } else {
            response = Response.status(Status.BAD_REQUEST).entity("Could not destroy session.").build();
        }
        log.info("[" + response.getStatus() + "] Logout for session id \"" + sessionToken + "\"");
        return response;
    }

}
