package org.jbpm.console.ng.services.shared;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.inject.Singleton;

import org.kie.internal.runtime.manager.RuntimeManager;
import org.kie.internal.runtime.manager.RuntimeManagerFactory;

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
public class DomainRuntimeManagerProviderImpl implements DomainRuntimeManagerProvider {

    /* (non-Javadoc)
     * @see org.jbpm.console.ng.services.shared.DomainRuntimeManagerProvider#getRuntimeManager(java.lang.String)
     */
    @Lock(LockType.READ)
    public RuntimeManager getRuntimeManager(String domainId) { 
        return null;
    }
    
}
