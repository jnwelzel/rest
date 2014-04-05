package com.jonwelzel.web.oauth;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

/**
 * {@link WebApplicationException Web application exception} that is mapped
 * either to {@link javax.ws.rs.core.Response.Status#BAD_REQUEST} (e.g. if
 * problem with OAuth parameters occurs) or
 * {@link javax.ws.rs.core.Response.Status#UNAUTHORIZED} (e.g. if signature is
 * incorrect).
 * 
 * @author Martin Matula
 * @author Miroslav Fuksa (miroslav.fuksa at oracle.com)
 */
public class OAuth1Exception extends WebApplicationException {
	private static final long serialVersionUID = 1L;
	private final Response.Status status;
	private final String wwwAuthHeader;
	private final String message;

	/**
	 * Create a new exception.
	 * 
	 * @param status Response status.
	 * @param wwwAuthHeader {@code Authorization} header value of the request
	 *            that cause the exception.
	 */
	public OAuth1Exception(Response.Status status, String wwwAuthHeader) {
		this.status = status;
		this.wwwAuthHeader = wwwAuthHeader;
		this.message = null;
	}

	public OAuth1Exception(String message) {
		this.wwwAuthHeader = null;
		this.status = Status.BAD_REQUEST;
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message != null ? message : super.getMessage();
	}

	/**
	 * Get the status of the error response.
	 * 
	 * @return Response status code.
	 */
	public Response.Status getStatus() {
		return status;
	}

	/**
	 * Get the {@code Authorization} header of the request that cause the
	 * exception.
	 * 
	 * @return Authorization header value.
	 */
	public String getWwwAuthHeader() {
		return wwwAuthHeader;
	}

	@Override
	public Response getResponse() {
		ResponseBuilder rb = Response.status(status);
		if (wwwAuthHeader != null) {
			rb.header("WWW-Authenticate", wwwAuthHeader);
		}
		return rb.build();
	}
}