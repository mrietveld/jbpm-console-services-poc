package org.jbpm.console.ng.services.rest;

import org.kie.internal.KieInternalServices;
import org.kie.internal.process.CorrelationKeyFactory;
import org.kie.internal.runtime.manager.Context;
import org.kie.internal.runtime.manager.Runtime;
import org.kie.internal.runtime.manager.RuntimeManager;
import org.kie.internal.runtime.manager.context.CorrelationKeyContext;
import org.kie.internal.runtime.manager.context.EmptyContext;
import org.kie.internal.runtime.manager.context.ProcessInstanceIdContext;

public abstract class AbstractRuntimeResource {

    protected RuntimeManager runtimeManager;
    protected Runtime kieRuntime;
    
    // The methods from the CorrelationKeyFactory instance are (at the moment) coincidentally thread-safe, 
    // but if that changes, there will be a problem (mriet, 03-2013)
    private static CorrelationKeyFactory keyFactory = KieInternalServices.Factory.get().newCorrelationKeyFactory();
    
    protected void init(RuntimeManager runtimeManager) {
        this.runtimeManager = runtimeManager;
        Context<?> managerContext = EmptyContext.get();
        this.kieRuntime = this.runtimeManager.getRuntime(managerContext);
    }

    protected void init(RuntimeManager runtimeManager, String ksessionId) {
        this.runtimeManager = runtimeManager;

        Context<?> managerContext;
        
        if (ksessionId == null) {
            managerContext = EmptyContext.get();
        } else {
            try {
                Long longId = Long.parseLong(ksessionId);
                managerContext = ProcessInstanceIdContext.get(longId);
            } catch (NumberFormatException nfe) {
                managerContext = CorrelationKeyContext.get(keyFactory.newCorrelationKey(ksessionId));
            }
        }
        
        this.kieRuntime = runtimeManager.getRuntime(managerContext);
    }
    
    protected void dispose() { 
        this.runtimeManager.disposeRuntime(this.kieRuntime);
        this.kieRuntime = null;
        this.runtimeManager = null;
    }
}
