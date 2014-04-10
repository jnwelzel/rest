package com.jonwelzel.web.resources;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;

import com.jonwelzel.commons.entities.OAuth1Consumer;
import com.jonwelzel.commons.entities.Token;
import com.jonwelzel.ejb.annotations.Log;
import com.jonwelzel.web.oauth.OAuth1Configuration;
import com.jonwelzel.web.oauth.OAuth1Exception;
import com.jonwelzel.web.oauth.OAuth1Parameters;
import com.jonwelzel.web.oauth.OAuth1ProviderImpl;
import com.jonwelzel.web.oauth.OAuth1Secrets;
import com.jonwelzel.web.oauth.OAuth1Signature;
import com.jonwelzel.web.oauth.OAuth1SignatureException;
import com.jonwelzel.web.oauth.OAuthServerRequest;

/**
 * Resource handling access token requests.
 * 
 * @author Hubert A. Le Van Gong <hubert.levangong at Sun.COM>
 * @author Martin Matula
 */

@Path(OAuth1Configuration.ACCESS_TOKEN_URL)
public class AccessTokenResource {
    @Inject
    private OAuth1ProviderImpl provider;

    @Inject
    private OAuth1Signature oAuth1Signature;

    @Inject
    @Log
    private Logger log;

    /**
     * POST method for creating a request for Request Token.
     * 
     * @return an HTTP response with content of the updated or created resource.
     */
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Token postAccessTokenRequest(@Context ContainerRequestContext requestContext, @Context Request req) {
        boolean sigIsOk = false;
        OAuthServerRequest request = new OAuthServerRequest(requestContext);
        OAuth1Parameters params = new OAuth1Parameters();
        params.readRequest(request);

        if (params.getToken() == null) {
            throw new WebApplicationException(new Throwable("oauth_token MUST be present."), 400);
        }

        String consKey = params.getConsumerKey();
        if (consKey == null) {
            throw new OAuth1Exception(Response.Status.BAD_REQUEST, null);
        }

        Token rt = provider.getRequestToken(params.getToken());
        if (rt == null) {
            // token invalid
            throw new OAuth1Exception(Response.Status.BAD_REQUEST, null);
        }

        OAuth1Consumer consumer = rt.getConsumer();
        if (consumer == null || !consKey.equals(consumer.getKey())) {
            // token invalid
            throw new OAuth1Exception(Response.Status.BAD_REQUEST, null);

        }

        OAuth1Secrets secrets = new OAuth1Secrets().consumerSecret(consumer.getSecret()).tokenSecret(rt.getSecret());
        try {
            sigIsOk = oAuth1Signature.verify(request, params, secrets);
        } catch (OAuth1SignatureException ex) {
            log.error(null, ex);
        }

        if (!sigIsOk) {
            // signature invalid
            log.error("Signature is invalid.");
            throw new OAuth1Exception(Response.Status.BAD_REQUEST, null);
        }

        // We're good to go.
        Token at = provider.newAccessToken(rt, params.getVerifier());

        if (at == null) {
            throw new OAuth1Exception(Response.Status.BAD_REQUEST, null);
        }

        // Preparing the response.
        final Token token = new Token(at.getToken(), at.getSecret());
        log.info("New access token \"oauth_token\"=" + at.getToken() + " \"oauth_token_secret\"=" + at.getSecret());
        return token;
    }
}
