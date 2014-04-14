package com.jonwelzel.commons.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonManagedReference;

@Entity
@NamedQueries({
        @NamedQuery(name = "Post.findAllByAlias", query = "SELECT p FROM Post p JOIN p.user u WHERE u.alias = :alias"),
        @NamedQuery(name = "Post.findAllByUser", query = "SELECT p FROM Post p JOIN p.user u WHERE u = :user"),
        @NamedQuery(name = "Post.findByIdAndUser", query = "SELECT p FROM Post p JOIN p.user u WHERE u = :user AND p.id = :id") })
public class Post extends AbstractEntity<Long> {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "post_seq", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_seq")
    @Column(name = "ID")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 10000)
    private String content;

    @ManyToOne
    @JsonBackReference(value = "author")
    private User user;

    @OneToMany(orphanRemoval = true, mappedBy = "post", cascade = CascadeType.REMOVE)
    @JsonManagedReference(value = "post")
    private List<Comment> comments = new ArrayList<>();

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

}
