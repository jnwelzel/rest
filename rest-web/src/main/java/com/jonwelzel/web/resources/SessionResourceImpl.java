package com.jonwelzel.web.resources;

import java.security.NoSuchAlgorithmException;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;

import com.jonwelzel.ejb.annotations.Log;
import com.jonwelzel.ejb.session.HttpSessionBean;
import com.jonwelzel.ejb.user.UserBean;
import com.jonwelzel.persistence.entities.User;
import com.jonwelzel.util.SecurityUtils;

@Path("session")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@DeclareRoles(value = { "USER", "ADMIN" })
@PermitAll
public class SessionResourceImpl implements SessionResource {

	@Inject
	@Log
	private Logger log;

	@Inject
	private UserBean userBean;

	@Inject
	private HttpSessionBean httpSessionBean;

	@Override
	@POST
	public Response login(User user) {
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
			httpSessionBean.newSession(hash, dbUser.getId().toString(), false);
			dbUser.setPassword(hash); // send back the session secret and not the user id, use the 'password' attr for
										// storing the session hash, that's all ;)
		} catch (NoSuchAlgorithmException e) {
			log.error("The \"SHA1\" encryption algorithm needed to generate the user session hash could not be found.");
			throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity("Could not secure user session.").build());
		}
		return Response.status(Status.OK).entity(dbUser).build();
	}

	@Override
	@DELETE
	public Response logout(@HeaderParam("authorization") String sessionToken) {
		int result = httpSessionBean.destroySession(sessionToken);
		Response response = null;
		if (result == 1 || result == 0) {
			response = Response.status(Status.OK).build();
		} else {
			response = Response.status(Status.BAD_REQUEST).entity("Could not destroy session.").build();
		}
		return response;
	}

}
