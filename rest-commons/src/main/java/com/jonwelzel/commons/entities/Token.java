package com.jonwelzel.commons.entities;

import java.security.Principal;
import java.util.Map;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
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
@NamedQueries(value = {
        @NamedQuery(name = "Token.findByToken", query = "SELECT t FROM Token t WHERE t.token = :token"),
        @NamedQuery(name = "Token.findByVerifier", query = "SELECT t FROM Token t WHERE t.verifier = :verifier") })
public class Token extends AbstractEntity<Long> implements OAuth1Token {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "token_seq", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "token_seq")
    @Column(name = "ID")
    private Long id;

    @Column(length = 40, nullable = false)
    private String token;

    @ManyToOne(optional = true, fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JsonBackReference(value = "user")
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JsonBackReference(value = "consumer")
    private Consumer consumer;

    @Column(length = 40)
    private String secret;

    @Column(length = 40)
    private String verifier;

    @Basic(optional = true)
    private String callbackUrl;

    public Token(String token, String secret) {
        this.token = token;
        this.secret = secret;
    }

    public Token() {
    }

    public Token(String token) {
        this.token = token;
    }

    public Token(String token, String secret, Consumer consumer, String callbackUrl) {
        this.token = token;
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
    public Long getId() {
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

    public String getVerifier() {
        return verifier;
    }

    public void setVerifier(String verifier) {
        this.verifier = verifier;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public void setToken(String token) {
        this.token = token;
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
        return token;
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
