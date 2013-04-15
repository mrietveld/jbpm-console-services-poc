package org.jbpm.console.ng.services.client.api.remote;

import java.lang.reflect.Proxy;

import org.jbpm.console.ng.services.client.api.AbstractApiRequestFactoryImpl;
import org.jbpm.console.ng.services.client.api.MessageHolder;
import org.jbpm.console.ng.services.client.api.ServiceRequestFactoryProvider;
import org.jbpm.console.ng.services.client.api.ServiceRequestFactoryProvider.RequestApiType;
import org.jbpm.console.ng.services.client.api.remote.api.KieSessionRequest;
import org.jbpm.console.ng.services.client.api.remote.api.TaskServiceRequest;
import org.jbpm.console.ng.services.client.message.ServiceMessage;

public class RemoteApiRequestFactoryImpl extends AbstractApiRequestFactoryImpl {

    static {
        ServiceRequestFactoryProvider.setInstance(RequestApiType.REMOTE, new RemoteApiRequestFactoryImpl());
    }

    private RemoteApiRequestFactoryImpl() {
        // private constructor
    }

    public TaskServiceRequest createTaskRequest(String domainName, String sessionId) {
        return internalCreateTaskRequest(domainName, sessionId);
    }

    public TaskServiceRequest createTaskRequest(String domainName) {
        return internalCreateTaskRequest(domainName, null);
    }

    public KieSessionRequest createConsoleKieSessionRequest(String domainName, String sessionId) {
        return internalCreateKieSessionRequest(domainName, sessionId);
    }

    public KieSessionRequest createKieSessionRequest(String domainName) {
        return internalCreateKieSessionRequest(domainName, null);
    }

    private TaskServiceRequest internalCreateTaskRequest(String domainName, String sessionid) {
        if( serializationProvider == null ) { 
            throw new IllegalStateException("Serialization provider must be set before creating a request.");
        }
        Class<?>[] interfaces = { TaskServiceRequest.class, MessageHolder.class };
        return (TaskServiceRequest) Proxy.newProxyInstance(ServiceMessage.class.getClassLoader(), interfaces,
                new RemoteApiServiceRequestProxy(domainName, sessionid, serializationProvider));
    }

    private KieSessionRequest internalCreateKieSessionRequest(String domainName, String sessionid) {
        if( serializationProvider == null ) { 
            throw new IllegalStateException("Serialization provider must be set before creating a request.");
        }
        Class<?>[] interfaces = { KieSessionRequest.class, MessageHolder.class };
        return (KieSessionRequest) Proxy.newProxyInstance(ServiceMessage.class.getClassLoader(), interfaces,
                new RemoteApiServiceRequestProxy(domainName, sessionid, serializationProvider));
    }

}