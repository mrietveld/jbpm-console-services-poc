package org.jbpm.console.ng.services.ejb.domain;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

import org.jbpm.runtime.manager.impl.DefaultRuntimeEnvironment;
import org.kie.internal.runtime.manager.RuntimeEnvironment;
import org.kie.internal.runtime.manager.cdi.qualifier.PerRequest;

@Singleton
@ApplicationScoped
public class RuntimeEnvironmentProvider {

    private RuntimeEnvironment singletonRuntimeEnvironment = new DefaultRuntimeEnvironment();
    
    @PostConstruct
    public void init() { 
       
    }
    
    @Produces
    @Singleton
    public RuntimeEnvironment getSingletonRuntimeEnvironment() { 
        return null;
    }
    
    @Produces
    @PerRequest
    public RuntimeEnvironment getPerRequestRuntimeEnvironment() { 
        return null;
    }
    
    @Produces
    @PerRequest
    public RuntimeEnvironment getPerProcessRuntimeEnvironment() { 
        return null;
    }
    
}
