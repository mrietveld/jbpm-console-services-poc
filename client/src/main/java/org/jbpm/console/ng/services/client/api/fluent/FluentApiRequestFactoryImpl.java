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
        ServiceRequestFactoryProvider.setInstance(RequestApiType.FLUENT, new FluentApiRequestFactoryImpl());
    }
    
    private FluentApiRequestFactoryImpl() { 
        // private constructor
    }

    public FluentTaskServiceRequest createTaskServiceRequest(String domainName, Long sessionId) {
        return (FluentTaskServiceRequest) internalCreateRequest(domainName, sessionId);
    }
    
    public FluentTaskServiceRequest createTaskServiceRequest(String domainName) {
        return (FluentTaskServiceRequest) internalCreateRequest(domainName, null);
    }
    
    public FluentTaskServiceRequest createTaskServiceRequest() {
        return (FluentTaskServiceRequest) internalCreateRequest(null, null);
    }
    
    public FluentKieSessionRequest createKieSessionRequest(String domainName, Long sessionId) {
        return (FluentKieSessionRequest) internalCreateRequest(domainName, sessionId);
    }
    
    public FluentKieSessionRequest createKieSessionRequest(String domainName) {
        return (FluentKieSessionRequest) internalCreateRequest(domainName, null);
    }
    
    public FluentKieSessionRequest createKieSessionRequest() {
        return (FluentKieSessionRequest) internalCreateRequest(null, null);
    }
    
    private Object internalCreateRequest(String domainName, Long sessionid) { 
        if( serializationProvider == null ) { 
            throw new IllegalStateException("Serialization provider must be set before creating a request.");
        }
        Class<?>[] interfaces = { FluentKieSessionRequest.class, FluentWorkItemManagerRequest.class, FluentTaskServiceRequest.class, MessageHolder.class };
        return Proxy.newProxyInstance(ServiceMessage.class.getClassLoader(), interfaces,
                new FluentApiServiceRequestProxy(domainName, serializationProvider));
    }

}