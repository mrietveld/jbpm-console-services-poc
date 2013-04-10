package org.jbpm.console.ng.services.jpa;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@ApplicationScoped
public class EntityManagerFactoryProducer {

    @Produces
    public EntityManagerFactory createPersistenceUnit() { 
        return Persistence.createEntityManagerFactory("org.jbpm.persistence.jpa");
    }
}
