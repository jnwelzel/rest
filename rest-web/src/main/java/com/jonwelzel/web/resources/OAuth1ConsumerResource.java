package com.jonwelzel.web.resources;

import static com.jonwelzel.web.oauth.OAuth1Endpoints.CONSUMER_ROOT_URL;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.jonwelzel.ejb.consumer.ConsumerBean;
import com.jonwelzel.persistence.entities.Consumer;

@Path(CONSUMER_ROOT_URL)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OAuth1ConsumerResource implements Resource<Long, Consumer> {

    @Inject
    private ConsumerBean consumerBean;

    @Override
    public List<Consumer> getResources() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Consumer getResource(Long id) {
        return consumerBean.findConsumer(id);
    }

    @Override
    public Consumer createResource(Consumer resource) {
        try {
            return consumerBean.createConsumer(resource);
        } catch (NoSuchAlgorithmException e) {
            throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity("Could not generate consumer keys.").build());
        }
    }

    @Override
    public Consumer updateResource(Consumer resource) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void deleteResource(Long id) {
        // TODO Auto-generated method stub

    }

}
