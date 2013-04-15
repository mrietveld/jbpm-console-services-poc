package org.jbpm.console.ng.services.ejb;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.enterprise.context.ApplicationScoped;

import org.jbpm.runtime.manager.api.RuntimeManagerCacheEntryPoint;
import org.kie.internal.runtime.manager.RuntimeManager;

/*
 * {@inheritdoc}
 */
@ApplicationScoped
public class RuntimeManagerCacheImpl implements RuntimeManagerCacheEntryPoint {

    private static ConcurrentHashMap<String, RuntimeManager> runtimeManagerMap = new ConcurrentHashMap<String, RuntimeManager>();
    
    @Override
    public void addDomainRuntimeManager(String domain, RuntimeManager runtimeManager) {
        runtimeManagerMap.put(domain, runtimeManager);
    }

    @Override
    public RuntimeManager replaceDomainRuntimeManager(String domain, RuntimeManager newRuntimeManager) {
        return runtimeManagerMap.replace(domain, newRuntimeManager);
    }

    @Override
    public RuntimeManager removeDomainRuntimeManager(String domain) {
        return runtimeManagerMap.remove(domain);
    }
    
    public RuntimeManager getRuntimeManager(String domainName) { 
       return runtimeManagerMap.get(domainName);
    }

    public RuntimeManager getRuntimeManager(String domainName, Long processInstanceId) { 
       return null; 
    }

    public void saveProcessInstanceIdDomainAndSession(Long processInstanceId, String domain, String sessionId) {
        
    }
}
