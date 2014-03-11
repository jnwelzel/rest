package com.jonwelzel.web.resources.user;

import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.jonwelzel.ejb.user.UserBean;
import com.jonwelzel.persistence.entities.User;
import com.jonwelzel.util.MediaType;
import com.jonwelzel.web.resources.Resource;

/**
 * Restful resource for {@link User}.
 * 
 * @author jwelzel
 * 
 */
@Path("/users")
@Consumes(MediaType.JSON)
@Produces(MediaType.JSON)
public class UserResource implements Resource<Long, User> {

    @EJB
    private UserBean userBean;

    @Override
    @GET
    public List<User> getResources() {
        List<User> users = userBean.findAll();
        users.add(new User(1L, "João da Conceição"));
        users.add(new User(2L, "Damião da Silva"));
        return users;
    }

    @Override
    @GET
    @Path("{id}")
    public User getResource(@PathParam("id") Long id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @POST
    public User createResource(User resource) {
        return userBean.createUser(resource);
    }

    @Override
    public User updateResource(User resouce) {
        // TODO Auto-generated method stub
        return null;
    }
}
