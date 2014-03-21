package com.jonwelzel.web.resources.user;

import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.jonwelzel.ejb.user.UserBean;
import com.jonwelzel.persistence.entities.User;
import com.jonwelzel.util.SecurityUtils;
import com.jonwelzel.web.resources.Resource;

/**
 * Restful resource for {@link User}.
 * 
 * @author jwelzel
 * 
 */
@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource implements Resource<Long, User> {

    @EJB
    private UserBean userBean;

    @Override
    @GET
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
    public User createResource(User resource) {
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

    @POST
    @Path("/login")
    public Response login(User user) {
        System.out.println("Log in for user \"" + user.getEmail() + "\"");
        User dbUser = userBean.findByEmail(user.getEmail());
        if (dbUser == null) {
            throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity("Invalid email address.")
                    .build());
        }
        if (!SecurityUtils.validatePassword(user.getPassword(), dbUser.getPasswordHash())) {
            throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity("Invalid password.").build());
        }
        return Response.status(Status.OK).entity(dbUser).build();
    }
}
