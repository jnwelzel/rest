package com.jonwelzel.ejb.post;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.jonwelzel.commons.dao.AbstractJpaDao;
import com.jonwelzel.commons.entities.Post;
import com.jonwelzel.commons.entities.User;

@Stateless
public class PostDao extends AbstractJpaDao<Long, Post> {

    @PersistenceContext(unitName = "rest")
    private EntityManager entityManager;

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    public List<Post> findAlByAlias(String alias) {
        TypedQuery<Post> query = getEntityManager().createNamedQuery("Post.findAllByAlias", Post.class);
        query.setParameter("alias", alias);
        try {
            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Post> findAlByUser(User user) {
        TypedQuery<Post> query = getEntityManager().createNamedQuery("Post.findAllByUser", Post.class);
        query.setParameter("user", user);
        try {
            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Post findByIdAndUser(Long postId, User user) {
        TypedQuery<Post> query = getEntityManager().createNamedQuery("Post.findByIdAndUser", Post.class);
        query.setParameter("id", postId);
        query.setParameter("user", user);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}
