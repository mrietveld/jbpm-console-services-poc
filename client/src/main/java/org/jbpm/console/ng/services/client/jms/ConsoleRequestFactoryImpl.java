package org.jbpm.console.ng.services.client.jms;

import java.lang.reflect.Proxy;

import org.kie.api.runtime.KieSession;
import org.kie.internal.task.api.TaskService;

class ConsoleRequestFactoryImpl extends ConsoleRequestFactory {

    ConsoleRequestFactoryImpl() { 

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
        Class<?>[] interfaces = { TaskService.class, ConsoleRequestHolder.class };
        return (TaskService) Proxy.newProxyInstance(ClientConsoleRequest.class.getClassLoader(), interfaces,
                new ConsoleRequestProxy(domainName, sessionid));
    }

    private KieSession internalCreateConsoleKieSessionRequest(String domainName, String sessionid) { 
        Class<?>[] interfaces = { KieSession.class, ConsoleRequestHolder.class };
        return (KieSession) Proxy.newProxyInstance(ClientConsoleRequest.class.getClassLoader(), interfaces,
                new ConsoleRequestProxy(domainName, sessionid));
    }

}