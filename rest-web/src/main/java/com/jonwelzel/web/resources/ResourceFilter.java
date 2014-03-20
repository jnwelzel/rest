package com.jonwelzel.web.resources;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import com.jonwelzel.ejb.oauth.AuthTokenBean;

@WebFilter(filterName = "ResourceFilter", urlPatterns = "/resources/*")
public class ResourceFilter implements Filter {

    @EJB
    private AuthTokenBean tokenBean;

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException,
            ServletException {
        // TODO Auto-generated method stub
        System.out.println("RESOURCEFILTER.DOFILTER()");
        filterChain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

}
