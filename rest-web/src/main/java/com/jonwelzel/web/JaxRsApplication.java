package com.jonwelzel.web;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.glassfish.jersey.server.mvc.freemarker.FreemarkerMvcFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jonwelzel.web.providers.SecurityFilter;
import com.jonwelzel.web.resources.OAuth1TokenResource;
import com.jonwelzel.web.resources.Oauth1AuthorizationResource;
import com.jonwelzel.web.resources.SessionResourceImpl;
import com.jonwelzel.web.resources.UserResource;

@ApplicationPath("/")
public class JaxRsApplication extends ResourceConfig {

    private Logger log = LoggerFactory.getLogger(getClass());

    private static final String JACKSON_FEATURE_CLASS = "org.glassfish.jersey.jackson.JacksonFeature";

    public JaxRsApplication() {
        log.info("Registering JAX-RS application resources...");
        registerComponents();
    }

    private void registerComponents() {
        // Resources
        register(UserResource.class);
        register(SessionResourceImpl.class);
        register(Oauth1AuthorizationResource.class);
        register(OAuth1TokenResource.class);

        // Sec + Auth
        register(SecurityFilter.class);
        register(RolesAllowedDynamicFeature.class); // Important for auth in JAX-RS using annotations

        // Others
        register(FreemarkerMvcFeature.class);
        property(FreemarkerMvcFeature.TEMPLATES_BASE_PATH, "freemarker");

        try {
            register(Class.forName(JACKSON_FEATURE_CLASS));
            log.info("Jackson Feature successfully registered!");
        } catch (ClassNotFoundException ignored) {
            log.error("Could not register Jackson Feature. Class \"" + JACKSON_FEATURE_CLASS + "\" not found.");
        }

    }

}
