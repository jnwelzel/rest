package com.jonwelzel.persistence.entities;

import java.security.Principal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

/**
 * Class representing a registered consumer, for OAuth 1 purposes.
 * 
 * @author jwelzel
 * 
 */
@Entity
public class Consumer extends AbstractEntity<Long> implements Principal {

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

    /**
     * Returns consumer key.
     * 
     * @return consumer key
     */
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
    String getSecret() {
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

    @Override
    public String toString() {
        return "Consumer{" + "id=" + id + ", name='" + getName() + "', email='" + getEmail() + "'}";
    }

}
