package org.jbpm.console.ng.services.client.api.fluent;

import java.lang.reflect.Proxy;

import org.jbpm.console.ng.services.client.api.AbstractApiRequestFactoryImpl;
import org.jbpm.console.ng.services.client.api.MessageHolder;
import org.jbpm.console.ng.services.client.api.fluent.api.FluentKieSessionRequest;
import org.jbpm.console.ng.services.client.api.fluent.api.FluentTaskServiceRequest;
import org.jbpm.console.ng.services.client.api.fluent.api.FluentWorkItemManagerRequest;
import org.jbpm.console.ng.services.client.jms.ServiceMessage;
import org.jbpm.console.ng.services.client.jms.ServiceRequestFactoryProvider;
import org.jbpm.console.ng.services.client.jms.ServiceRequestFactoryProvider.RequestApiType;

public class FluentApiRequestFactoryImpl extends AbstractApiRequestFactoryImpl {

    static { 
        ServiceRequestFactoryProvider.setInstance(RequestApiType.REMOTE, new FluentApiRequestFactoryImpl());
    }
    
    private FluentApiRequestFactoryImpl() { 
        // private constructor
    }

    public FluentTaskServiceRequest createConsoleTaskRequest(String domainName, String sessionId) {
        return internalCreateConsoleTaskRequest(domainName, sessionId);
    }
    
    public FluentTaskServiceRequest createConsoleTaskRequest(String domainName) {
        return internalCreateConsoleTaskRequest(domainName, null);
    }
    
    public FluentKieSessionRequest createConsoleKieSessionRequest(String domainName, String sessionId) {
        return internalCreateConsoleKieSessionRequest(domainName, sessionId);
    }
    
    public FluentKieSessionRequest createConsoleKieSessionRequest(String domainName) {
        return internalCreateConsoleKieSessionRequest(domainName, null);
    }
    
    private FluentTaskServiceRequest internalCreateConsoleTaskRequest(String domainName, String sessionid) { 
        if( serializationProvider == null ) { 
            throw new IllegalStateException("Serialization provider must be set before creating a request.");
        }
        Class<?>[] interfaces = { FluentTaskServiceRequest.class, MessageHolder.class };
        return (FluentTaskServiceRequest) Proxy.newProxyInstance(ServiceMessage.class.getClassLoader(), interfaces,
                new FluentApiServiceRequestProxy(domainName, sessionid, serializationProvider ));
    }

    private FluentKieSessionRequest internalCreateConsoleKieSessionRequest(String domainName, String sessionid) { 
        if( serializationProvider == null ) { 
            throw new IllegalStateException("Serialization provider must be set before creating a request.");
        }
        Class<?>[] interfaces = { FluentKieSessionRequest.class, FluentWorkItemManagerRequest.class, MessageHolder.class };
        return (FluentKieSessionRequest) Proxy.newProxyInstance(ServiceMessage.class.getClassLoader(), interfaces,
                new FluentApiServiceRequestProxy(domainName, sessionid, serializationProvider));
    }

}