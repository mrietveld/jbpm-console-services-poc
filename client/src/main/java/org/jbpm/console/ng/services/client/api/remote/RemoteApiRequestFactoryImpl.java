package org.jbpm.console.ng.services.client.api.remote;

import java.lang.reflect.Proxy;

import org.jbpm.console.ng.services.client.api.ClientRequestHolder;
import org.jbpm.console.ng.services.client.api.remote.api.KieSessionRequest;
import org.jbpm.console.ng.services.client.api.remote.api.TaskServiceRequest;
import org.jbpm.console.ng.services.client.jms.ServiceRequest;
import org.jbpm.console.ng.services.client.jms.ServiceRequestFactoryProvider;
import org.jbpm.console.ng.services.client.jms.ServiceRequestFactoryProvider.RequestApiType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.task.api.TaskService;

public class RemoteApiRequestFactoryImpl {

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
        Class<?>[] interfaces = { TaskServiceRequest.class, ClientRequestHolder.class };
        return (TaskServiceRequest) Proxy.newProxyInstance(ServiceRequest.class.getClassLoader(), interfaces,
                new RemoteApiServiceRequestProxy(domainName, sessionid));
    }

    private KieSessionRequest internalCreateConsoleKieSessionRequest(String domainName, String sessionid) { 
        Class<?>[] interfaces = { KieSessionRequest.class, ClientRequestHolder.class };
        return (KieSessionRequest) Proxy.newProxyInstance(ServiceRequest.class.getClassLoader(), interfaces,
                new RemoteApiServiceRequestProxy(domainName, sessionid));
    }

}