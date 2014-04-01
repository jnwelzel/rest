package com.jonwelzel.web.endpoints;

import java.util.Arrays;
import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import org.slf4j.Logger;

import com.jonwelzel.ejb.annotations.Log;
import com.jonwelzel.ejb.user.UserBean;
import com.jonwelzel.persistence.entities.User;
import com.jonwelzel.persistence.enumerations.RoleType;
import com.jonwelzel.web.Resource;

/**
 * Restful resource for {@link User}.
 * 
 * @author jwelzel
 * 
 */
@Path("users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@DeclareRoles(value = { "USER", "ADMIN" })
@RolesAllowed(value = { "USER", "ADMIN" })
@RequestScoped
public class UserResource implements Resource<Long, User> {

    @Inject
    @Log
    private Logger log;

    @Inject
    private UserBean userBean;

    @Override
    @GET
    @PermitAll
    public List<User> getResources(@HeaderParam("authorization") String token, @Context SecurityContext securityContext) {
        log.info("Authorization token: " + token);
        if (securityContext.getUserPrincipal() != null) {
            log.info("User principal: " + securityContext.getUserPrincipal().getName());
        }
        return userBean.findAll();
    }

    @Override
    @GET
    @Path("{id}")
    public User getResource(@PathParam("id") Long id, @HeaderParam("authorization") String token) {
        // TODO Tratar com um 404 NOT FOUND caso n exista User com id passado
        log.info("Authorization token: " + token);
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
