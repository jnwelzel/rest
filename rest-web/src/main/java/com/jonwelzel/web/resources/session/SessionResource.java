package com.jonwelzel.web.resources.session;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.jonwelzel.persistence.entities.User;

@Path("/session")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@DeclareRoles(value = { "USER", "ADMIN" })
@PermitAll
public interface SessionResource {

    @POST
    public Response login(User user);

    @DELETE
    public Response logout(@HeaderParam("authorization") String sessionToken);

}
