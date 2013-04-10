package org.jbpm.console.ng.services.client.api.same.jms;

import java.lang.reflect.Proxy;

import org.jbpm.console.ng.services.client.api.ClientRequestHolder;
import org.jbpm.console.ng.services.client.jms.ServiceClientRequest;
import org.jbpm.console.ng.services.client.jms.ServiceRequestFactoryProvider;
import org.jbpm.console.ng.services.client.jms.ServiceRequestFactoryProvider.RequestApiType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.task.api.TaskService;

public class SameApiRequestFactoryImpl {

    static { 
        ServiceRequestFactoryProvider.setInstance(RequestApiType.ORIGINAL, new SameApiRequestFactoryImpl());
    }
    
    private SameApiRequestFactoryImpl() { 
        // private constructor
    }
    
    public TaskService createConsoleTaskRequest(String domainName, String sessionId) {
        return internalCreateConsoleTaskRequest(domainName, sessionId);
    }
    
    public TaskService createConsoleTaskRequest(String domainName) {
        return internalCreateConsoleTaskRequest(domainName, null);
    }
    
    public KieSession createConsoleKieSessionRequest(String domainName, String sessionId) {
        return internalCreateConsoleKieSessionRequest(domainName, sessionId);
    }
    
    public KieSession createConsoleKieSessionRequest(String domainName) {
        return internalCreateConsoleKieSessionRequest(domainName, null);
    }
    
    private TaskService internalCreateConsoleTaskRequest(String domainName, String sessionid) { 
        Class<?>[] interfaces = { TaskService.class, ClientRequestHolder.class };
        return (TaskService) Proxy.newProxyInstance(ServiceClientRequest.class.getClassLoader(), interfaces,
                new SameApiRequestProxy(domainName, sessionid));
    }

    private KieSession internalCreateConsoleKieSessionRequest(String domainName, String sessionid) { 
        Class<?>[] interfaces = { KieSession.class, ClientRequestHolder.class };
        return (KieSession) Proxy.newProxyInstance(ServiceClientRequest.class.getClassLoader(), interfaces,
                new SameApiRequestProxy(domainName, sessionid));
    }

}