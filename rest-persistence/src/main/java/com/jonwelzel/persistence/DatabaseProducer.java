package com.jonwelzel.persistence;

import javax.ejb.Stateless;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@RequestScoped
@Stateless
public class DatabaseProducer {

    private static EntityManagerFactory factory = Persistence.createEntityManagerFactory("rest");

    @Produces
    @Database
    public EntityManager getEntityManager() {
        return factory.createEntityManager();
    }

}
