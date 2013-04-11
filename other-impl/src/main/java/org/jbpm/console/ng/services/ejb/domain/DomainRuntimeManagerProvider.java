package org.jbpm.console.ng.services.ejb.domain;

import org.kie.internal.runtime.manager.RuntimeEnvironment;
import org.kie.internal.runtime.manager.RuntimeManager;

public class DomainRuntimeManagerProvider {

    public RuntimeManager getRuntimeManager(String domainName) { 
        return getRuntimeManager(domainName, null);
    }
    
    public RuntimeManager getRuntimeManager(String domainName, String processInstanceId) { 
        // RuntimeEnvironment is dependent on domain 
        // for example: "Business/Per Request" has a new environment (reload BPMN2 every time) 
        //               - doesn't matter which domain -- new env every time
        //              "Development/Singleton" uses the same environment
        //               - but it must be the "Development" environment
        //              "Acceptance/Singleton" uses the same environment
        //               - but it must be the "Acceptance" environment
        //              "Other/Per Process" uses the environment for the process instance
        //               - if no runEnv exists for domain/processInstanceid, make a new one
        //               - otherwise, retrieve existing one
        RuntimeEnvironment runtimeEnvironment = getRuntimeEnvironment(domainName, processInstanceId);
        
        // RuntimeManager is dependent on domain, runtimeEnvironment
        // for example: "Business/Per Request" 
        //               - this method is called once per request, so just make a new one
        //              "Development/Singleton" uses the same runtimeManager
        //               - initial runtimeManager dependent on "Development" runtimeEnvironment
        //               - after that, retrieve using domainName
        //              "Other/Per Process" 
        //               - initial runtimeManager dependent on (domain + processInstanceId) runtimeEnvironment
        //               - after that, retrieve using domainName/processInstanceId mapping
        return internalGetRuntimeManager(domainName, runtimeEnvironment, processInstanceId);
    }
    
    private RuntimeEnvironment getRuntimeEnvironment(String domainName, String processInstanceId) { 
        return null;
    }
    
    private RuntimeManager internalGetRuntimeManager(String domainName, RuntimeEnvironment runtimeEnvironment, String processInstanceId) { 
        return null;
    }
    
}
