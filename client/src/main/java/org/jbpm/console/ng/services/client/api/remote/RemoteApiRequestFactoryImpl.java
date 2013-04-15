package org.jbpm.console.ng.services.client.api.remote;

import java.lang.reflect.Proxy;

import org.jbpm.console.ng.services.client.api.AbstractApiRequestFactoryImpl;
import org.jbpm.console.ng.services.client.api.MessageHolder;
import org.jbpm.console.ng.services.client.api.remote.api.KieSessionRequest;
import org.jbpm.console.ng.services.client.api.remote.api.TaskServiceRequest;
import org.jbpm.console.ng.services.client.jms.ServiceMessage;
import org.jbpm.console.ng.services.client.jms.ServiceRequestFactoryProvider;
import org.jbpm.console.ng.services.client.jms.ServiceRequestFactoryProvider.RequestApiType;

public class RemoteApiRequestFactoryImpl extends AbstractApiRequestFactoryImpl {

    static {
        ServiceRequestFactoryProvider.setInstance(RequestApiType.REMOTE, new RemoteApiRequestFactoryImpl());
    }

    private RemoteApiRequestFactoryImpl() {
        // private constructor
    }

    public TaskServiceRequest createConsoleTaskRequest(String domainName, String sessionId) {
        return internalCreateConsoleTaskRequest(domainName, sessionId);
    }

    public TaskServiceRequest createConsoleTaskRequest(String domainName) {
        return internalCreateConsoleTaskRequest(domainName, null);
    }

    public KieSessionRequest createConsoleKieSessionRequest(String domainName, String sessionId) {
        return internalCreateConsoleKieSessionRequest(domainName, sessionId);
    }

    public KieSessionRequest createConsoleKieSessionRequest(String domainName) {
        return internalCreateConsoleKieSessionRequest(domainName, null);
    }

    private TaskServiceRequest internalCreateConsoleTaskRequest(String domainName, String sessionid) {
        if( serializationProvider == null ) { 
            throw new IllegalStateException("Serialization provider must be set before creating a request.");
        }
        Class<?>[] interfaces = { TaskServiceRequest.class, MessageHolder.class };
        return (TaskServiceRequest) Proxy.newProxyInstance(ServiceMessage.class.getClassLoader(), interfaces,
                new RemoteApiServiceRequestProxy(domainName, sessionid, serializationProvider));
    }

    private KieSessionRequest internalCreateConsoleKieSessionRequest(String domainName, String sessionid) {
        if( serializationProvider == null ) { 
            throw new IllegalStateException("Serialization provider must be set before creating a request.");
        }
        Class<?>[] interfaces = { KieSessionRequest.class, MessageHolder.class };
        return (KieSessionRequest) Proxy.newProxyInstance(ServiceMessage.class.getClassLoader(), interfaces,
                new RemoteApiServiceRequestProxy(domainName, sessionid, serializationProvider));
    }

}