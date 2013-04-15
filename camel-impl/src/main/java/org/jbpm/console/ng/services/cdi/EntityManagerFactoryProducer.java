package org.jbpm.console.ng.services.cdi;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@ApplicationScoped
public class EntityManagerFactoryProducer {

    private static EntityManagerFactory emf;
    
   
    @PostConstruct
    public void init() { 
        emf = Persistence.createEntityManagerFactory("org.jbpm.persistence.jpa");
    }
    
    @Produces
    public EntityManager createPersistenceUnit() { 
        return emf.createEntityManager();
    }
}
