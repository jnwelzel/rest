package com.jonwelzel.commons.entities;

import java.security.Principal;
import java.util.Map;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonBackReference;

import com.jonwelzel.commons.enumerations.RoleType;

/**
 * Model/pojo for authentication tokens (OAuth). It tells what application has access to what user's resources.
 * 
 * @author jwelzel
 * 
 */
@Entity
@Table(name = "TOKEN")
public class Token extends AbstractEntity<String> implements OAuth1Token {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID", length = 40)
    private String id;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JsonBackReference(value = "user")
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JsonBackReference(value = "consumer")
    private Consumer consumer;

    @Column(length = 40)
    private String secret;

    @Basic(optional = true)
    private String callbackUrl;

    public Token() {
    }

    public Token(String id) {
        this.id = id;
    }

    public Token(String id, String secret, Consumer consumer, String callbackUrl) {
        this.id = id;
        this.secret = secret;
        this.consumer = consumer;
        this.callbackUrl = callbackUrl;
    }

    /**
     * Returns string representing the token.
     * 
     * @return string representing the token
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * Returns consumer this token was issued for.
     * 
     * @return consumer this token was issued for.
     */
    @Override
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
    @Override
    public String getSecret() {
        return secret;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns a {@link User} object containing the name of the user the request containing this token is authorized to
     * act on behalf of. When the oauth filter verifies the request with this token is properly authenticated, it
     * injects this token into a security context which then delegates
     * {@link javax.ws.rs.core.SecurityContext#getUserPrincipal()} to this method.
     * 
     * @return User corresponding to this token, or null if the token is not authorized
     */
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Returns callback URL for this token (applicable just to request tokens)
     * 
     * @return callback url
     */
    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    @Override
    public String getToken() {
        return id;
    }

    @Override
    public Map<String, String> getAttributes() {
        return null;
    }

    @Override
    public Principal getPrincipal() {
        return user == null ? consumer : user;
    }

    @Override
    public boolean isInRole(String role) {
        return user == null ? (consumer.getRoles().contains(RoleType.valueOf(role))) : (user.getRoles()
                .contains(RoleType.valueOf(role)));
    }

}
