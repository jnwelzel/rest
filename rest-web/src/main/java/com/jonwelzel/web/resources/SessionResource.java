package com.jonwelzel.web.resources;

import javax.ws.rs.core.Response;

import com.jonwelzel.commons.entities.User;

public interface SessionResource {

    public Response login(User user);

    public Response logout(String sessionToken);

}
