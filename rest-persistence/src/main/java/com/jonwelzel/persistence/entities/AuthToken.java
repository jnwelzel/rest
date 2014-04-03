package com.jonwelzel.persistence.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonBackReference;

/**
 * Model/pojo for authentication token. It tells what application (3rd party or not) has access to what user resource.
 * 
 * @author jwelzel
 * 
 */
@Entity
@Table(name = "AUTH_TOKEN")
public class AuthToken extends AbstractEntity<String> {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID", length = 40)
    private String id;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JsonBackReference
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Consumer consumer;

    @Column(length = 40)
    private String secret;

    public AuthToken() {
    }

    public AuthToken(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    /**
     * Returns the token secret.
     * 
     * @return token secret
     */
    String getSecret() {
        return secret;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
