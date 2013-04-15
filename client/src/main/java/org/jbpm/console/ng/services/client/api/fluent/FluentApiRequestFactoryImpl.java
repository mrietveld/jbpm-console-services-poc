package org.jbpm.console.ng.services.client.api.fluent;

import java.lang.reflect.Proxy;

import org.jbpm.console.ng.services.client.api.AbstractApiRequestFactoryImpl;
import org.jbpm.console.ng.services.client.api.MessageHolder;
import org.jbpm.console.ng.services.client.api.ServiceRequestFactoryProvider;
import org.jbpm.console.ng.services.client.api.ServiceRequestFactoryProvider.RequestApiType;
import org.jbpm.console.ng.services.client.api.fluent.api.FluentKieSessionRequest;
import org.jbpm.console.ng.services.client.api.fluent.api.FluentTaskServiceRequest;
import org.jbpm.console.ng.services.client.api.fluent.api.FluentWorkItemManagerRequest;
import org.jbpm.console.ng.services.client.message.ServiceMessage;

public class FluentApiRequestFactoryImpl extends AbstractApiRequestFactoryImpl {

    static { 
        ServiceRequestFactoryProvider.setInstance(RequestApiType.REMOTE, new FluentApiRequestFactoryImpl());
    }
    
    private FluentApiRequestFactoryImpl() { 
        // private constructor
    }

    public FluentTaskServiceRequest createTaskRequest(String domainName, String sessionId) {
        return internalCreateTaskRequest(domainName, sessionId);
    }
    
    public FluentTaskServiceRequest createTaskRequest(String domainName) {
        return internalCreateTaskRequest(domainName, null);
    }
    
    public FluentKieSessionRequest createKieSessionRequest(String domainName, String sessionId) {
        return internalCreateKieSessionRequest(domainName, sessionId);
    }
    
    public FluentKieSessionRequest createKieSessionRequest(String domainName) {
        return internalCreateKieSessionRequest(domainName, null);
    }
    
    private FluentTaskServiceRequest internalCreateTaskRequest(String domainName, String sessionid) { 
        if( serializationProvider == null ) { 
            throw new IllegalStateException("Serialization provider must be set before creating a request.");
        }
        Class<?>[] interfaces = { FluentTaskServiceRequest.class, MessageHolder.class };
        return (FluentTaskServiceRequest) Proxy.newProxyInstance(ServiceMessage.class.getClassLoader(), interfaces,
                new FluentApiServiceRequestProxy(domainName, sessionid, serializationProvider ));
    }

    private FluentKieSessionRequest internalCreateKieSessionRequest(String domainName, String sessionid) { 
        if( serializationProvider == null ) { 
            throw new IllegalStateException("Serialization provider must be set before creating a request.");
        }
        Class<?>[] interfaces = { FluentKieSessionRequest.class, FluentWorkItemManagerRequest.class, MessageHolder.class };
        return (FluentKieSessionRequest) Proxy.newProxyInstance(ServiceMessage.class.getClassLoader(), interfaces,
                new FluentApiServiceRequestProxy(domainName, sessionid, serializationProvider));
    }

}