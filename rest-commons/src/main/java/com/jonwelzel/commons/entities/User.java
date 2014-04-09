package com.jonwelzel.commons.entities;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonManagedReference;

import com.jonwelzel.commons.enumerations.RoleType;

/**
 * Application user model/pojo.
 * 
 * @author jwelzel
 * 
 */
@Entity
@Table(name = "APP_USER")
@NamedQueries(value = {
        @NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email = :email"),
        @NamedQuery(name = "User.findByToken", query = "SELECT u FROM User u JOIN u.tokens t WHERE t.token = :authToken") })
public class User extends AbstractEntity<Long> implements Principal {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "app_user_seq", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_user_seq")
    @Column(name = "ID")
    private Long id;

    @Column(length = 128, nullable = false)
    private String firstName;

    @Column(length = 128, nullable = false)
    private String lastName;

    @Column(length = 30, unique = true, nullable = false)
    private String alias;

    @Column(length = 80, nullable = false, unique = true)
    private String email;

    @Transient
    private String password;

    @Column(nullable = false)
    private String passwordHash;

    @ManyToMany(mappedBy = "users")
    private List<Company> companies;

    @OneToMany(cascade = { CascadeType.REMOVE }, orphanRemoval = true, mappedBy = "user")
    @JsonManagedReference(value = "user")
    private List<Token> tokens = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @OneToMany(orphanRemoval = true)
    private List<RoleType> roles;

    public User() {
    }

    public User(Long id, String name) {
        this.id = id;
        this.firstName = name;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String name) {
        this.firstName = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Company> getCompanies() {
        return companies;
    }

    public void setCompanies(List<Company> companies) {
        this.companies = companies;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public void setTokens(List<Token> tokens) {
        this.tokens = tokens;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public List<RoleType> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleType> roles) {
        this.roles = roles;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", name='" + getFirstName() + " " + getLastName() + "', email='" + getEmail()
                + "', username='" + getAlias() + "'}";
    }

    @Override
    public String getName() {
        return firstName + " " + lastName;
    }
}
