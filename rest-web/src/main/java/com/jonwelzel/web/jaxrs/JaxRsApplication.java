package com.jonwelzel.web.jaxrs;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jonwelzel.web.jaxrs.security.SecurityContextFilter;
import com.jonwelzel.web.resources.session.SessionResourceImpl;
import com.jonwelzel.web.resources.user.UserResource;

@ApplicationPath(value = "/resources")
public class JaxRsApplication extends Application {

    private Logger log = LoggerFactory.getLogger(getClass());
    private static final String JACKSON_FEATURE_CLASS = "org.glassfish.jersey.jackson.JacksonFeature";

    @Override
    public Set<Class<?>> getClasses() {
        final HashSet<Class<?>> set = new HashSet<Class<?>>(3);
        set.add(UserResource.class);
        set.add(SessionResourceImpl.class);
        set.add(SecurityContextFilter.class);
        try {
            set.add(Class.forName(JACKSON_FEATURE_CLASS));
        } catch (ClassNotFoundException ignored) {
            log.warn("Could not find class \"" + JACKSON_FEATURE_CLASS + "\"");
        }
        return set;
    }

}
