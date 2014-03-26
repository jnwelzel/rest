package com.jonwelzel.web.jaxrs.security;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;

public class SecurityContextFilter implements ResourceFilter, ContainerRequestFilter {

    @Override
    public ContainerRequest filter(ContainerRequest arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ContainerRequestFilter getRequestFilter() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ContainerResponseFilter getResponseFilter() {
        // TODO Auto-generated method stub
        return null;
    }

}
