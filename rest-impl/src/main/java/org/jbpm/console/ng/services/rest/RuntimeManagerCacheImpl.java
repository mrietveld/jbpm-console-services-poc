package org.jbpm.console.ng.services.rest;

import org.jbpm.runtime.manager.api.RuntimeManagerCacheEntryPoint;
import org.kie.internal.runtime.manager.RuntimeManager;

public class RuntimeManagerCacheImpl implements RuntimeManagerCacheEntryPoint {

    @Override
    public void addDomainRuntimeManager(String domain, RuntimeManager runtimeManager) {
        // DBG Auto-generated method stub

    }

    @Override
    public RuntimeManager replaceDomainRuntimeManager(String domain, RuntimeManager runtimeManager) {
        // DBG Auto-generated method stub
        return null;
    }

    @Override
    public RuntimeManager removeDomainRuntimeManager(String domain) {
        // DBG Auto-generated method stub
        return null;
    }
    
    public RuntimeManager getRuntimeManager(String domainName) { 
       return null; 
    }

    public RuntimeManager getRuntimeManager(String domainName, Long processInstanceId) { 
       return null; 
    }

}
