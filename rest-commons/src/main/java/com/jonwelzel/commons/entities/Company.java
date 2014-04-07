package com.jonwelzel.commons.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Company model/pojo.
 * 
 * @author jwelzel
 * 
 */
@Entity
@Table(name = "COMPANY")
public class Company extends AbstractEntity<Long> {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "company_seq", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "company_seq")
    @Column(name = "ID")
    private Long id;

    @ManyToOne(optional = false)
    private User owner;

    @ManyToMany
    @JoinTable(name = "COMPANY_USERS", joinColumns = { @JoinColumn(name = "COMPANY_ID", referencedColumnName = "ID") }, inverseJoinColumns = { @JoinColumn(name = "APP_USER_ID", referencedColumnName = "ID") })
    private List<User> users;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

}
