package org.jbpm.console.ng.services.client.api.same;

import java.lang.reflect.Proxy;

import org.jbpm.console.ng.services.client.api.AbstractApiRequestFactoryImpl;
import org.jbpm.console.ng.services.client.api.MessageHolder;
import org.jbpm.console.ng.services.client.api.ServiceRequestFactoryProvider;
import org.jbpm.console.ng.services.client.api.ServiceRequestFactoryProvider.RequestApiType;
import org.jbpm.console.ng.services.client.message.ServiceMessage;
import org.jbpm.console.ng.services.client.message.serialization.MessageSerializationProvider;
import org.kie.api.runtime.KieSession;
import org.kie.internal.task.api.TaskService;

public class SameApiRequestFactoryImpl extends AbstractApiRequestFactoryImpl {

    static { 
        ServiceRequestFactoryProvider.setInstance(RequestApiType.ORIGINAL, new SameApiRequestFactoryImpl());
    }
    
    private SameApiRequestFactoryImpl() { 
        // private constructor
    }
    
    public TaskService createTaskRequest(String domainName, String sessionId) {
        return internalCreateTaskRequest(domainName, sessionId);
    }
    
    public TaskService createTaskRequest(String domainName) {
        return internalCreateTaskRequest(domainName, null);
    }
    
    public KieSession createKieSessionRequest(String domainName, String sessionId) {
        return internalCreateKieSessionRequest(domainName, sessionId);
    }
    
    public KieSession createKieSessionRequest(String domainName) {
        return internalCreateKieSessionRequest(domainName, null);
    }
    
    private TaskService internalCreateTaskRequest(String domainName, String sessionid) { 
        if( serializationProvider == null ) { 
            throw new IllegalStateException("Serialization provider must be set before creating a request.");
        }
        Class<?>[] interfaces = { TaskService.class, MessageHolder.class };
        return (TaskService) Proxy.newProxyInstance(ServiceMessage.class.getClassLoader(), interfaces,
                new SameApiRequestProxy(domainName, sessionid, serializationProvider));
    }

    private KieSession internalCreateKieSessionRequest(String domainName, String sessionid) { 
        if( serializationProvider == null ) { 
            throw new IllegalStateException("Serialization provider must be set before creating a request.");
        }
        Class<?>[] interfaces = { KieSession.class, MessageHolder.class };
        return (KieSession) Proxy.newProxyInstance(ServiceMessage.class.getClassLoader(), interfaces,
                new SameApiRequestProxy(domainName, sessionid, serializationProvider));
    }

}