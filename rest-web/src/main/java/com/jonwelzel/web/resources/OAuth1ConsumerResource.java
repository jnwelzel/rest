package com.jonwelzel.web.resources;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import com.jonwelzel.commons.entities.Consumer;
import com.jonwelzel.ejb.consumer.ConsumerBean;

@Path("consumers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OAuth1ConsumerResource implements Resource<Long, Consumer> {

    @Inject
    private ConsumerBean consumerBean;

    @Override
    @GET
    public List<Consumer> getResources() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @GET
    @Path("{id}")
    public Consumer getResource(@PathParam("id") Long id) {
        return consumerBean.findConsumer(id);
    }

    @Override
    @POST
    public Consumer createResource(Consumer resource, @Context SecurityContext securityContext) {
        try {
            return consumerBean.createConsumer(resource);
        } catch (NoSuchAlgorithmException e) {
            throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity("Could not generate consumer keys.").build());
        }
    }

    @Override
    @PUT
    public Consumer updateResource(Consumer resource) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @DELETE
    public void deleteResource(Long id) {
        // TODO Auto-generated method stub

    }

}
