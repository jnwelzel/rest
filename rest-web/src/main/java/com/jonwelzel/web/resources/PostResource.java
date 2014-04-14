package com.jonwelzel.web.resources;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import com.jonwelzel.commons.entities.Post;
import com.jonwelzel.commons.entities.User;
import com.jonwelzel.ejb.post.PostBean;

@Path("posts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed({ "USER", "ADMIN" })
public class PostResource {

    @Inject
    private PostBean postBean;

    @POST
    public Post create(@Context SecurityContext sc, Post post) {
        User user = (User) sc.getUserPrincipal();
        post.setUser(user);
        return postBean.create(post);
    }

    @GET
    public List<Post> myPosts(@Context SecurityContext sc) {
        return postBean.findAllByUser((User) sc.getUserPrincipal());
    }

    @GET
    @Path("{id}")
    public Post getPost(@PathParam("id") Long postId, @Context SecurityContext sc) {
        return postBean.findByIdAndUser(postId, (User) sc.getUserPrincipal());
    }

    @GET
    @Path("{alias}")
    public List<Post> userPosts(@PathParam("alias") String alias) {
        return postBean.findAllByAlias(alias);
    }

    @GET
    @Path("{alias}/{id}")
    public Post getUserPost(@PathParam("alias") String alias, @PathParam("id") Long id) {
        return postBean.find(id);
    }
}
