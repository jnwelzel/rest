package com.jonwelzel.web.oauth;

import java.net.URL;
import java.util.List;
import java.util.Set;

/**
 * Interface to be implemented as a wrapper around an HTTP request, so that digital signature can be generated and/or
 * verified.
 * 
 * @author Paul C. Bryan <pbryan@sun.com>
 */
public interface OAuth1Request {

    /**
     * Returns the name of the HTTP method with which this request was made, for example, GET, POST, or PUT.
     * 
     * @return the name of the method with which this request was made.
     */
    public String getRequestMethod();

    /**
     * Returns the URL of the request, including protocol, server name, optional port number, and server path.
     * 
     * @return the request URL.
     */
    public URL getRequestURL();

    /**
     * Returns an {@link java.util.Set} of {@link String} objects containing the names of the parameters contained in
     * the request.
     * 
     * @return the names of the parameters.
     */
    public Set<String> getParameterNames();

    /**
     * Returns an {@link java.util.List} of {@link String} objects containing the values of the specified request
     * parameter, or null if the parameter does not exist. For HTTP requests, parameters are contained in the query
     * string and/or posted form data.
     * 
     * @param name
     *            the name of the parameter.
     * @return the values of the parameter.
     */
    public List<String> getParameterValues(String name);

    /**
     * Returns the value(s) of the specified request header. If the request did not include a header of the specified
     * name, this method returns null.
     * 
     * @param name
     *            the header name.
     * @return the value(s) of the requested header, or null if none exist.
     */
    public List<String> getHeaderValues(String name);

    /**
     * Adds a header with the given name and value.
     * 
     * @param name
     *            the name of the header.
     * @param value
     *            the header value.
     * @throws IllegalStateException
     *             if this method cannot be implemented.
     */
    public void addHeaderValue(String name, String value) throws IllegalStateException;
}