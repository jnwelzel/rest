package com.jonwelzel.web.resources;

import static com.jonwelzel.web.oauth.OAuth1Endpoints.ACCESS_TOKEN_URL;
import static com.jonwelzel.web.oauth.OAuth1Endpoints.REQUEST_TOKEN_URL;
import static com.jonwelzel.web.oauth.OAuth1Endpoints.TOKEN_ROOT_URL;
import static javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;

import com.jonwelzel.ejb.annotations.Log;
import com.jonwelzel.ejb.consumer.ConsumerBean;
import com.jonwelzel.ejb.oauth.AuthTokenBean;
import com.jonwelzel.persistence.entities.AuthToken;
import com.jonwelzel.persistence.entities.Consumer;
import com.jonwelzel.web.oauth.OAuth1Exception;
import com.jonwelzel.web.oauth.OAuth1Parameters;
import com.jonwelzel.web.oauth.OAuth1Secrets;
import com.jonwelzel.web.oauth.OAuth1Signature;
import com.jonwelzel.web.oauth.OAuth1SignatureException;
import com.jonwelzel.web.oauth.OAuthServerRequest;

@Path(TOKEN_ROOT_URL)
public class OAuth1TokenResource {

    @Inject
    private OAuth1Signature oAuth1Signature;

    @Inject
    private AuthTokenBean authTokenBean;

    @Inject
    @Log
    private Logger log;

    @Inject
    private ConsumerBean consumerBean;

    @Path(ACCESS_TOKEN_URL)
    public Response accessToken() {
        return null;
    }

    @POST
    @Path(REQUEST_TOKEN_URL)
    @Consumes(APPLICATION_FORM_URLENCODED)
    @Produces(APPLICATION_FORM_URLENCODED)
    public AuthToken newRequestToken(@Context ContainerRequestContext requestContext) {
        // Request validation comes first
        OAuthServerRequest request = new OAuthServerRequest(requestContext);
        OAuth1Parameters params = new OAuth1Parameters();
        params.readRequest(request);
        String tok = params.getToken();
        if ((tok != null) && (!tok.contentEquals(""))) {
            throw new OAuth1Exception(Response.Status.BAD_REQUEST, null);
        }

        String consKey = params.getConsumerKey();
        if (consKey == null) {
            throw new OAuth1Exception(Response.Status.BAD_REQUEST, null);
        }

        Consumer consumer = consumerBean.findByKey(consKey);
        if (consumer == null) {
            throw new OAuth1Exception(Response.Status.BAD_REQUEST, null);
        }
        OAuth1Secrets secrets = new OAuth1Secrets().consumerSecret(consumer.getSecret()).tokenSecret("");

        boolean sigIsOk = false;
        try {
            sigIsOk = oAuth1Signature.verify(request, params, secrets);
        } catch (OAuth1SignatureException ex) {
            log.error(null, ex);
        }

        if (!sigIsOk) {
            throw new OAuth1Exception(Response.Status.BAD_REQUEST, null);
        }

        MultivaluedMap<String, String> parameters = new MultivaluedHashMap<String, String>();
        for (String n : request.getParameterNames()) {
            parameters.put(n, request.getParameterValues(n));
        }

        // If all is good persist this token
        AuthToken token = authTokenBean.newRequestToken(consKey, params.getCallback());
        if (token == null) {
            throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity("Could not generate security information.").build());
        }
        return token;
    }
}
