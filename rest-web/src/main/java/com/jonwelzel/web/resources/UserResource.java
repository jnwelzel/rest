package com.jonwelzel.web.resources;

import java.util.Arrays;
import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import org.slf4j.Logger;

import com.jonwelzel.commons.entities.User;
import com.jonwelzel.commons.enumerations.RoleType;
import com.jonwelzel.ejb.annotations.Log;
import com.jonwelzel.ejb.user.UserBean;

/**
 * Restful resource for {@link User}.
 * 
 * @author jwelzel
 * 
 */
@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@DeclareRoles(value = { "USER", "ADMIN" })
@RolesAllowed(value = { "USER", "ADMIN" })
public class UserResource implements BaseResource<Long, User> {

    @Inject
    @Log
    private Logger log;

    @Inject
    private UserBean userBean;

    @Override
    @GET
    @PermitAll
    public List<User> getResources() {
        return userBean.findAll();
    }

    @Override
    @GET
    @Path("{id}")
    public User getResource(@PathParam("id") Long id) {
        // TODO Tratar com um 404 NOT FOUND caso n exista User com id passado
        return userBean.findUser(id);
    }

    @Override
    @POST
    @PermitAll
    public User createResource(User resource, @Context SecurityContext securityContext) {
        if (!securityContext.isUserInRole(RoleType.ADMIN.toString())) {
            resource.setRoles(Arrays.asList(RoleType.USER)); // Only admin can make other admin
        }
        return userBean.createUser(resource);
    }

    @Override
    @PUT
    public User updateResource(User resource) {
        return userBean.updateUser(resource);
    }

    @Override
    @DELETE
    @Path("{id}")
    public void deleteResource(@PathParam("id") Long id) {
        userBean.deleteUser(id);
    }

}
