package com.jonwelzel.persistence.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Model/pojo for authentication token.
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

    @OneToOne(mappedBy = "authToken")
    private User user;

    @Override
    public String getId() {
        return id;
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
