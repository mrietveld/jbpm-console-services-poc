package org.jbpm.console.ng.services.client.api.same;

import java.lang.reflect.Proxy;

import org.jbpm.console.ng.services.client.api.AbstractApiRequestFactoryImpl;
import org.jbpm.console.ng.services.client.api.MessageHolder;
import org.jbpm.console.ng.services.client.api.ServiceRequestFactoryProvider;
import org.jbpm.console.ng.services.client.api.ServiceRequestFactoryProvider.RequestApiType;
import org.jbpm.console.ng.services.client.message.ServiceMessage;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.internal.task.api.TaskService;

public class SameApiRequestFactoryImpl extends AbstractApiRequestFactoryImpl {

    static { 
        ServiceRequestFactoryProvider.setInstance(RequestApiType.ORIGINAL, new SameApiRequestFactoryImpl());
    }
    
    private SameApiRequestFactoryImpl() { 
        // private constructor
    }
    
    public TaskService createTaskRequest(String domainName, Long sessionId) {
        return (TaskService) internalCreateRequest(domainName, sessionId);
    }
    
    public TaskService createTaskRequest(String domainName) {
        return (TaskService) internalCreateRequest(domainName, null);
    }
    
    public KieSession createKieSessionRequest(String domainName, Long  sessionId) {
        return (KieSession) internalCreateRequest(domainName, sessionId);
    }
    
    public KieSession createKieSessionRequest(String domainName) {
        return (KieSession) internalCreateRequest(domainName, null);
    }
    
    private Object internalCreateRequest(String domainName, Long sessionid) { 
        if( serializationProvider == null ) { 
            throw new IllegalStateException("Serialization provider must be set before creating a request.");
        }
        Class<?>[] interfaces = { KieSession.class, WorkItemHandler.class, TaskService.class, MessageHolder.class };
        return Proxy.newProxyInstance(ServiceMessage.class.getClassLoader(), interfaces,
                new SameApiServiceRequestProxy(domainName, sessionid, serializationProvider));
    }

}