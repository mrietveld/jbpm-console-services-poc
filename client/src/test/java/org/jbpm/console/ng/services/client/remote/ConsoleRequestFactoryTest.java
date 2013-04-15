package org.jbpm.console.ng.services.client.remote;

import org.jbpm.console.ng.services.client.api.MessageHolder;
import org.jbpm.console.ng.services.client.api.remote.RemoteApiRequestFactoryImpl;
import org.jbpm.console.ng.services.client.api.remote.api.KieSessionRequest;
import org.jbpm.console.ng.services.client.api.remote.api.TaskServiceRequest;
import org.jbpm.console.ng.services.client.jms.ServiceMessage;
import org.jbpm.console.ng.services.client.jms.ServiceMessage.OperationMessage;
import org.jbpm.console.ng.services.client.jms.ServiceRequestFactoryProvider;
import org.jbpm.console.ng.services.client.jms.serialization.MessageSerializationProvider.Type;
import org.junit.Assert;
import org.junit.Test;
import org.kie.internal.task.api.TaskService;

public class ConsoleRequestFactoryTest extends Assert { 

    private RemoteApiRequestFactoryImpl getConsoleRequestFactory() { 
        RemoteApiRequestFactoryImpl factory = ServiceRequestFactoryProvider.createNewRemoteApiInstance();
        factory.setSerialization(Type.MAP_MESSAGE);
        return factory;
    }
    
    @Test
    public void basicRequestShouldBeCreatedAndContainInfo() { 
       TaskServiceRequest taskServiceRequest = getConsoleRequestFactory().createConsoleTaskRequest("release", "correl-1");
       
       long taskId = 3;
       String userId = "bob";
       taskServiceRequest.activate(taskId, userId);
       
       ServiceMessage request = ((MessageHolder) taskServiceRequest).getRequest();
       
       assertTrue(request != null); 
      
       // test method name
       OperationMessage operRequest = request.getOperations().get(0);
       assertEquals("activate", operRequest.getMethodName());
       assertEquals(ServiceMessage.TASK_SERVICE_REQUEST, operRequest.getServiceType());
       
       // test args
       Object [] args = operRequest.getArgs();
       assertTrue(args != null && args.length == 2 );
       assertTrue((Long) args[0] == taskId);
       assertTrue(userId.equals(args[1]));
    }
    
    @Test
    public void shouldBeAbleToCreateKieSessionRequestAndAddInfo() { 
       KieSessionRequest kieSessionRequest = getConsoleRequestFactory().createConsoleKieSessionRequest("domain");

       String processName = "example-process";
       kieSessionRequest.startProcess("example-process");
       
       ServiceMessage request = ((MessageHolder) kieSessionRequest).getRequest();
       
       assertTrue(request != null); 
      
       // test method name
       OperationMessage operRequest = request.getOperations().get(0);
       assertTrue("startProcess".equals(operRequest.getMethodName()));
       
       // test args
       Object [] args = operRequest.getArgs();
       assertTrue(args != null && args.length == 1 );
       assertTrue(processName.equals(args[0]));
    }
    
    @Test
    public void shouldBeAbleToAddMultipleOperations() { 
        KieSessionRequest kieSessionRequest = getConsoleRequestFactory().createConsoleKieSessionRequest("domain");
        
        kieSessionRequest.startProcess("test");
        kieSessionRequest.signalEvent("party-event", null);
    }
    
}
