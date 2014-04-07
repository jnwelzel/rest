package com.jonwelzel.web.resources;

import static com.jonwelzel.web.oauth.OAuth1Configuration.AUTHORIZATION_ROOT_URL;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import org.glassfish.jersey.server.mvc.Viewable;
import org.slf4j.Logger;

import com.jonwelzel.commons.entities.Consumer;
import com.jonwelzel.commons.entities.Token;
import com.jonwelzel.commons.entities.User;
import com.jonwelzel.commons.utils.SecurityUtils;
import com.jonwelzel.ejb.annotations.Log;
import com.jonwelzel.ejb.consumer.ConsumerBean;
import com.jonwelzel.ejb.oauth.AuthTokenBean;
import com.jonwelzel.ejb.session.HttpSessionBean;
import com.jonwelzel.ejb.user.UserBean;
import com.jonwelzel.web.oauth.OAuth1Exception;
import com.jonwelzel.web.oauth.OAuth1Parameters;
import com.jonwelzel.web.oauth.OAuth1Secrets;
import com.jonwelzel.web.oauth.OAuth1Signature;
import com.jonwelzel.web.oauth.OAuth1SignatureException;
import com.jonwelzel.web.oauth.OAuthServerRequest;

@Path(AUTHORIZATION_ROOT_URL)
public class Oauth1AuthorizationResource {

    @Inject
    @Log
    private Logger log;

    @Inject
    private UserBean userBean;

    @Inject
    private HttpSessionBean httpSessionBean;

    @Inject
    private AuthTokenBean authTokenBean;

    @Inject
    private OAuth1Signature oAuth1Signature;

    @Inject
    private ConsumerBean consumerBean;

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response authorize(@Context ContainerRequestContext requestContext, @Context Request req) {
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

        Token rt = authTokenBean.find(params.getToken());
        if (rt == null) {
            // token invalid
            throw new OAuth1Exception(Response.Status.BAD_REQUEST, null);
        }

        Consumer consumer = rt.getConsumer();
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
            throw new OAuth1Exception(Response.Status.BAD_REQUEST, null);
        }

        // We're good to go.
        Token at = authTokenBean.newAccessToken(rt, params.getVerifier());

        if (at == null) {
            throw new OAuth1Exception(Response.Status.BAD_REQUEST, null);
        }

        // Preparing the response.
        Form resp = new Form();
        resp.param(OAuth1Parameters.TOKEN, at.getId());
        resp.param(OAuth1Parameters.TOKEN_SECRET, at.getSecret());
        return Response.ok(resp).build();
    }

    @GET
    public Viewable getHtml(@QueryParam("oauth_token") String oauthToken,
            @QueryParam("session_token") String sessionToken) {
        if (oauthToken == null || "".equals(oauthToken)) {
            throw new OAuth1Exception("\"oauth_token\" must be informed as a query parameter.");
        }
        Consumer consumer = consumerBean.findByToken(oauthToken);
        if (sessionToken == null || "".equals(sessionToken)) {
            Map<String, String> model = new HashMap<>();
            model.put("consumerApplicationName", consumer.getApplicationName());
            model.put("consumerDomain", consumer.getApplicationUrl());
            model.put("oauthToken", oauthToken);
            return new Viewable("/authorization/login", model);
        } else {
            Long userId = Long.valueOf(httpSessionBean.getUserId(sessionToken));
            User dbUser = userBean.findUser(userId);
            Map<String, Object> model = new HashMap<>();
            model.put("consumerApplicationName", consumer.getApplicationName());
            model.put("consumerDomain", consumer.getApplicationUrl());
            model.put("consumerApplicationDescription", consumer.getApplicationDescription());
            model.put("oauthToken", oauthToken);
            model.put("sessionToken", sessionToken);
            model.put("user", dbUser);
            return new Viewable("/authorization/authorize", model);
        }
    }

    @Path("login")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(User user, @QueryParam("oauth_token") String oauthToken,
            @Context SecurityContext securityContext) {
        if (oauthToken == null || "".equals(oauthToken)) {
            throw new OAuth1Exception("\"oauth_token\" must be informed as a query parameter.");
        }

        User dbUser = userBean.findByEmail(user.getEmail());
        if (dbUser == null) {
            throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity("Invalid email address.")
                    .build());
        }
        if (!SecurityUtils.validatePassword(user.getPassword(), dbUser.getPasswordHash())) {
            throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity("Invalid password.").build());
        }
        try { // Now the session stuff
            String hash = SecurityUtils.generateSecureHex();
            httpSessionBean.newSession(hash, dbUser.getId().toString(), false);
            Map<String, Object> model = new HashMap<>();
            model.put("oauthToken", oauthToken);
            model.put("sessionToken", hash);
            return Response.status(Status.OK).entity(model).build();
        } catch (NoSuchAlgorithmException e) {
            log.error("The \"SHA1\" encryption algorithm needed to generate the user session hash could not be found.");
            throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity("Could not secure user session.").build());
        }
    }

}
