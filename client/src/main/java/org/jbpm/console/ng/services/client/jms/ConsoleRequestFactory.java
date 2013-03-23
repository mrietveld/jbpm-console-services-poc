package org.jbpm.console.ng.services.client.jms;

import org.kie.api.runtime.KieSession;
import org.kie.internal.task.api.TaskService;

public abstract class ConsoleRequestFactory {

    public abstract TaskService createConsoleTaskRequest(String domainName);
    
    public abstract TaskService createConsoleTaskRequest(String domainName, String sessionId);
    
    public abstract KieSession createConsoleKieSessionRequest(String domainName);
    
    public abstract KieSession createConsoleKieSessionRequest(String domainName, String sessionId);
    
    public static ConsoleRequestFactory createNewInstance() { 
        return new ConsoleRequestFactoryImpl();
    }
    
}
