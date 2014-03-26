package com.jonwelzel.web.resources.user;

import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jonwelzel.ejb.user.UserBean;
import com.jonwelzel.persistence.entities.User;
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

    private Logger log = LoggerFactory.getLogger(getClass());

    @EJB
    private UserBean userBean;

    @Override
    @GET
    public List<User> getResources(@HeaderParam("authorization") String token) {
        log.info("Authorization token: " + token);
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

}
