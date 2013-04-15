package org.jbpm.console.ng.services.rest;

import javax.inject.Inject;

import org.jbpm.runtime.manager.api.RuntimeManagerCacheEntryPoint;
import org.kie.internal.KieInternalServices;
import org.kie.internal.process.CorrelationKeyFactory;
import org.kie.internal.runtime.manager.Context;
import org.kie.internal.runtime.manager.RuntimeEngine;
import org.kie.internal.runtime.manager.RuntimeManager;
import org.kie.internal.runtime.manager.context.CorrelationKeyContext;
import org.kie.internal.runtime.manager.context.EmptyContext;
import org.kie.internal.runtime.manager.context.ProcessInstanceIdContext;

public abstract class AbstractRuntimeResource {

    private String domainName;

    @Inject
    RuntimeManagerCacheEntryPoint runtimeManagerCache;
    
    // The methods from the CorrelationKeyFactory instance are (at the moment) coincidentally thread-safe, 
    // but if that changes, there will be a problem (mriet, 03-2013)
    private static CorrelationKeyFactory keyFactory = KieInternalServices.Factory.get().newCorrelationKeyFactory();
   
    public void setDomainName(String domainName) { 
        this.domainName = domainName;
    }
    
    protected RuntimeEngine getRuntimeEngine() { 
        return getRuntimeEngine(null, null);
    }
    
    protected RuntimeEngine getRuntimeEngine(String sessionId) { 
        return getRuntimeEngine(sessionId, null);
    }
    
    protected RuntimeEngine getRuntimeEngine(String sessionId, Long processInstanceId) { 
        RuntimeManager runtimeManager = ((RuntimeManagerCacheImpl) runtimeManagerCache).getRuntimeManager(this.domainName);
        Context<?> runtimeContext = getRuntimeManagerContext(sessionId, processInstanceId);
        return runtimeManager.getRuntimeEngine(runtimeContext);
    }
    
    private Context<?> getRuntimeManagerContext(String kieSessionId, Long processInstanceId) { 
        Context<?> managerContext;
        
        if( processInstanceId != null ) { 
            managerContext = new ProcessInstanceIdContext(processInstanceId);
        } else if (kieSessionId == null) {
            managerContext = EmptyContext.get();
        } else {
            try {
                Long longId = Long.parseLong(kieSessionId);
                managerContext = ProcessInstanceIdContext.get(longId);
            } catch (NumberFormatException nfe) {
                managerContext = CorrelationKeyContext.get(keyFactory.newCorrelationKey(kieSessionId));
            }
        }
        
        return managerContext;
    }
    
}
