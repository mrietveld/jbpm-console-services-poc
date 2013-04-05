package org.jbpm.console.ng.services.domain;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

/**
 * This is the "mock" class that does the following: <ul>
 * <li>Finds or creates the proper {@link RuntimeManagerFactory} instance for the 
 * domain given as a parameter</li>
 * </ul>
 * 
 * </p>This class is the link to the console that takes care of much of this for us.
 */
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class DomainRuntimeManagerProvider {

    @Lock(LockType.READ)
    public Object getRuntimeManager(String domainId) { 
        return null;
    }
    
    @Produces
    public DomainRuntimeManagerProvider getInstance() { 
        return this;
    }
    
}
