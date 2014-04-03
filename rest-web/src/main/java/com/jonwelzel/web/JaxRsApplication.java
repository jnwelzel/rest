package com.jonwelzel.web;

import java.util.HashSet;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jonwelzel.web.resources.SessionResourceImpl;
import com.jonwelzel.web.resources.UserResource;
import com.jonwelzel.web.security.SecurityFilter;

@ApplicationPath("/")
public class JaxRsApplication extends ResourceConfig {

    private Logger log = LoggerFactory.getLogger(getClass());

    private static final String JACKSON_FEATURE_CLASS = "org.glassfish.jersey.jackson.JacksonFeature";

    public JaxRsApplication() {
        log.info("Registering JAX-RS application resources...");
        registerClasses();
    }

    public void registerClasses() {

        final HashSet<Class<?>> set = new HashSet<Class<?>>();

        // Resources
        set.add(UserResource.class);
        set.add(SessionResourceImpl.class);

        // Sec + Auth
        set.add(SecurityFilter.class);
        set.add(RolesAllowedDynamicFeature.class); // Important for auth in JAX-RS using annotations

        try {
            set.add(Class.forName(JACKSON_FEATURE_CLASS));
            log.info("Jackson Feature successfully registered!");
        } catch (ClassNotFoundException ignored) {
            log.error("Could not register Jackson Feature. Class \"" + JACKSON_FEATURE_CLASS + "\" not found.");
        }

        registerClasses(set);
    }

}
