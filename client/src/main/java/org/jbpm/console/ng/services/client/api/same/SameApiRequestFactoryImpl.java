package org.jbpm.console.ng.services.client.api.same;

import java.lang.reflect.Proxy;

import org.jbpm.console.ng.services.client.api.AbstractApiRequestFactoryImpl;
import org.jbpm.console.ng.services.client.api.MessageHolder;
import org.jbpm.console.ng.services.client.jms.ServiceMessage;
import org.jbpm.console.ng.services.client.jms.ServiceRequestFactoryProvider;
import org.jbpm.console.ng.services.client.jms.ServiceRequestFactoryProvider.RequestApiType;
import org.jbpm.console.ng.services.client.jms.serialization.MessageSerializationProvider;
import org.kie.api.runtime.KieSession;
import org.kie.internal.task.api.TaskService;

public class SameApiRequestFactoryImpl extends AbstractApiRequestFactoryImpl {

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
        if( serializationProvider == null ) { 
            throw new IllegalStateException("Serialization provider must be set before creating a request.");
        }
        Class<?>[] interfaces = { TaskService.class, MessageHolder.class };
        return (TaskService) Proxy.newProxyInstance(ServiceMessage.class.getClassLoader(), interfaces,
                new SameApiRequestProxy(domainName, sessionid, serializationProvider));
    }

    private KieSession internalCreateConsoleKieSessionRequest(String domainName, String sessionid) { 
        if( serializationProvider == null ) { 
            throw new IllegalStateException("Serialization provider must be set before creating a request.");
        }
        Class<?>[] interfaces = { KieSession.class, MessageHolder.class };
        return (KieSession) Proxy.newProxyInstance(ServiceMessage.class.getClassLoader(), interfaces,
                new SameApiRequestProxy(domainName, sessionid, serializationProvider));
    }

}