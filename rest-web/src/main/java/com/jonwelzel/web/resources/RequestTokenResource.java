package com.jonwelzel.web.resources;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import com.jonwelzel.commons.entities.OAuth1Consumer;
import com.jonwelzel.commons.entities.OAuth1Token;
import com.jonwelzel.web.oauth.OAuth1Configuration;
import com.jonwelzel.web.oauth.OAuth1Exception;
import com.jonwelzel.web.oauth.OAuth1Parameters;
import com.jonwelzel.web.oauth.OAuth1Provider;
import com.jonwelzel.web.oauth.OAuth1Secrets;
import com.jonwelzel.web.oauth.OAuth1Signature;
import com.jonwelzel.web.oauth.OAuth1SignatureException;
import com.jonwelzel.web.oauth.OAuthServerRequest;

@Path(OAuth1Configuration.REQUEST_TOKEN_URL)
public class RequestTokenResource {
    @Inject
    private OAuth1Provider provider;

    @Inject
    private OAuth1Signature oAuth1Signature;

    /**
     * POST method for creating a request for a Request Token.
     * 
     * @return an HTTP response with content of the updated or created resource.
     */
    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/x-www-form-urlencoded")
    public Response postReqTokenRequest(@Context ContainerRequestContext requestContext) {
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

        OAuth1Consumer consumer = provider.getConsumer(consKey);
        if (consumer == null) {
            throw new OAuth1Exception(Response.Status.BAD_REQUEST, null);
        }
        OAuth1Secrets secrets = new OAuth1Secrets().consumerSecret(consumer.getSecret()).tokenSecret("");

        boolean sigIsOk = false;
        try {
            sigIsOk = oAuth1Signature.verify(request, params, secrets);
        } catch (OAuth1SignatureException ex) {
            Logger.getLogger(RequestTokenResource.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (!sigIsOk) {
            throw new OAuth1Exception(Response.Status.BAD_REQUEST, null);
        }

        MultivaluedMap<String, String> parameters = new MultivaluedHashMap<String, String>();
        for (String n : request.getParameterNames()) {
            parameters.put(n, request.getParameterValues(n));
        }

        OAuth1Token rt = provider.newRequestToken(consKey, params.getCallback(), parameters);

        Form resp = new Form();
        resp.param(OAuth1Parameters.TOKEN, rt.getToken());
        resp.param(OAuth1Parameters.TOKEN_SECRET, rt.getSecret());
        resp.param(OAuth1Parameters.CALLBACK_CONFIRMED, "true");
        return Response.ok(resp).build();
    }
}
