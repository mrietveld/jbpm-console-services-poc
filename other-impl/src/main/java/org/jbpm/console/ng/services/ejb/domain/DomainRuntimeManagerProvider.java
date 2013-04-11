package org.jbpm.console.ng.services.ejb.domain;

import org.kie.internal.runtime.manager.RuntimeEnvironment;
import org.kie.internal.runtime.manager.RuntimeManager;

public class DomainRuntimeManagerProvider {

    public RuntimeManager getRuntimeManager(String domainName) { 
        String strategy = getRuntimeStrategy(domainName);
        RuntimeEnvironment runtimeEnvironment = getRuntimeEnvironment(domainName, strategy);
        return internalGetRuntimeManager(domainName, strategy, runtimeEnvironment);
    }
    
    private RuntimeEnvironment getRuntimeEnvironment(String domainName, String strategy) { 
        return null;
    }
    
    private String getRuntimeStrategy(String domainName) { 
        return null;
    }
    
    private RuntimeManager internalGetRuntimeManager(String domainName, String strategy, RuntimeEnvironment runtimeEnvironment) { 
        return null;
    }
    
}
