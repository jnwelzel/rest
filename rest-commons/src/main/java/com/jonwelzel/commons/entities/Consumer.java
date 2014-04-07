package com.jonwelzel.commons.entities;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonManagedReference;

import com.jonwelzel.commons.enumerations.RoleType;

/**
 * Class representing a registered consumer, for OAuth 1 purposes.
 * 
 * @author jwelzel
 * 
 */
@Entity
@NamedQueries(value = {
        @NamedQuery(name = "Consumer.findByKey", query = "SELECT c from Consumer c where c.key = :key"),
        @NamedQuery(name = "Consumer.findByToken", query = "SELECT c FROM Consumer c JOIN c.authTokens a WHERE a.id = :authToken") })
public class Consumer extends AbstractEntity<Long> implements Principal, OAuth1Consumer {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "consumer_seq", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "consumer_seq")
    @Column(name = "ID")
    private Long id;

    @Column(nullable = false, unique = true, length = 40)
    private String name;

    @Column(nullable = false, unique = true, length = 80)
    private String email;

    @Column(length = 40, nullable = false, unique = true)
    private String key;

    @Column(length = 40, nullable = false, unique = true)
    private String secret;

    @Column(nullable = false, unique = true)
    private String applicationUrl;

    @Column(nullable = false, unique = true)
    private String applicationName;

    @Column(nullable = false, length = 2000)
    private String applicationDescription;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "consumer")
    @JsonManagedReference(value = "consumer")
    private List<Token> authTokens = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @OneToMany(orphanRemoval = true)
    private List<RoleType> roles;

    /**
     * Returns consumer key.
     * 
     * @return consumer key
     */
    @Override
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Returns consumer secret.
     * 
     * @return consumer secret
     */
    @Override
    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getApplicationUrl() {
        return applicationUrl;
    }

    public void setApplicationUrl(String applicationUrl) {
        this.applicationUrl = applicationUrl;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getApplicationDescription() {
        return applicationDescription;
    }

    public void setApplicationDescription(String applicationDescription) {
        this.applicationDescription = applicationDescription;
    }

    public List<RoleType> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleType> roles) {
        this.roles = roles;
    }

    public List<Token> getAuthTokens() {
        return authTokens;
    }

    public void setAuthTokens(List<Token> authTokens) {
        this.authTokens = authTokens;
    }

    @Override
    public String toString() {
        return "Consumer{" + "id=" + id + ", name='" + getName() + "', email='" + getEmail() + "'}";
    }

    @Override
    @JsonIgnore
    public Principal getPrincipal() {
        return this;
    }

    @Override
    public boolean isInRole(String role) {
        return roles.contains(RoleType.valueOf(role));
    }

}
