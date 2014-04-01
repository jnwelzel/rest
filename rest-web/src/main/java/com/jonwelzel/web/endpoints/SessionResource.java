package com.jonwelzel.web.endpoints;

import javax.ws.rs.core.Response;

import com.jonwelzel.persistence.entities.User;

public interface SessionResource {

    public Response login(User user);

    public Response logout(String sessionToken);

}
