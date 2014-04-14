package com.jonwelzel.ejb.post;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.jonwelzel.commons.entities.Post;
import com.jonwelzel.commons.entities.User;
import com.jonwelzel.ejb.annotations.Log;

@Stateless
@LocalBean
public class PostBean {

    @Inject
    @Log
    private Logger log;

    @Inject
    private PostDao postDao;

    public Post create(Post post) {
        log.info("Creating new post \"" + post.getTitle() + "\" for user \"" + post.getUser().getAlias() + "\"");
        return postDao.save(post);
    }

    public List<Post> findAllByAlias(String alias) {
        return postDao.findAlByAlias(alias);
    }

    public List<Post> findAllByUser(User user) {
        return postDao.findAlByUser(user);
    }

    public Post find(Long id) {
        return postDao.find(id);
    }

    public Post findByIdAndUser(Long postId, User user) {
        return postDao.findByIdAndUser(postId, user);
    }
}
