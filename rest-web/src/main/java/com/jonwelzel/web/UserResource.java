package com.jonwelzel.web;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.jonwelzel.ejb.user.UserLocalBusiness;
import com.jonwelzel.persistence.entities.User;
import com.jonwelzel.util.MediaType;

/**
 * {@link User} resource.
 * 
 * @author jwelzel
 * 
 */
@Path("/users")
@Stateless
@LocalBean
public class UserResource implements Resource<Long, User> {

    @Inject
    private UserLocalBusiness userBean;

    @Override
    @GET
    @Produces(MediaType.JSON)
    public List<User> getResources() {
        List<User> users = userBean.findAll();
        users.add(new User(1L, "João da Conceição"));
        users.add(new User(2L, "Damião da Silva"));
        // GenericEntity<List<User>> entity = new GenericEntity<List<User>>(users){};
        return users;
    }

    @Override
    @GET
    @Path("{id}")
    @Produces(MediaType.JSON)
    public User getResource(@PathParam("id") Long id) {
        // TODO Auto-generated method stub
        return null;
    }
}
