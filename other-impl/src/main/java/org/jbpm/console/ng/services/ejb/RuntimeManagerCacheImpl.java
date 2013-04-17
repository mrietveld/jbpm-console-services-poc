package org.jbpm.console.ng.services.ejb;

import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ApplicationScoped;

import org.kie.internal.runtime.manager.RuntimeManager;
import org.kie.internal.runtime.manager.RuntimeManagerCacheEntryPoint;

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

}
