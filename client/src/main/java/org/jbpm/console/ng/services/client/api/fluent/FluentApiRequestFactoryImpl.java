package org.jbpm.console.ng.services.client.api.fluent;

import java.lang.reflect.Proxy;

import org.jbpm.console.ng.services.client.api.ClientRequestHolder;
import org.jbpm.console.ng.services.client.api.fluent.api.FluentKieSessionRequest;
import org.jbpm.console.ng.services.client.api.fluent.api.FluentTaskServiceRequest;
import org.jbpm.console.ng.services.client.jms.ServiceClientRequest;
import org.jbpm.console.ng.services.client.jms.ServiceRequestFactoryProvider;
import org.jbpm.console.ng.services.client.jms.ServiceRequestFactoryProvider.RequestApiType;

public class FluentApiRequestFactoryImpl {

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
        Class<?>[] interfaces = { FluentTaskServiceRequest.class, ClientRequestHolder.class };
        return (FluentTaskServiceRequest) Proxy.newProxyInstance(ServiceClientRequest.class.getClassLoader(), interfaces,
                new FluentApiServiceRequestProxy(domainName, sessionid));
    }

    private FluentKieSessionRequest internalCreateConsoleKieSessionRequest(String domainName, String sessionid) { 
        Class<?>[] interfaces = { FluentKieSessionRequest.class, ClientRequestHolder.class };
        return (FluentKieSessionRequest) Proxy.newProxyInstance(ServiceClientRequest.class.getClassLoader(), interfaces,
                new FluentApiServiceRequestProxy(domainName, sessionid));
    }

}