package com.jonwelzel.web.endpoints;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jonwelzel.web.security.SecurityFilter;

@ApplicationPath("/")
public class JaxRsApplication extends Application {

    private Logger log = LoggerFactory.getLogger(getClass());

    private static final String JACKSON_FEATURE_CLASS = "org.glassfish.jersey.jackson.JacksonFeature";

    @Override
    public Set<Class<?>> getClasses() {
        log.info("Registering JAX-RS application classes...");

        final HashSet<Class<?>> set = new HashSet<Class<?>>(3);

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
            log.warn("Could not register Jackson Feature. Class \"" + JACKSON_FEATURE_CLASS + "\" not found.");
        }
        return set;
    }

}
