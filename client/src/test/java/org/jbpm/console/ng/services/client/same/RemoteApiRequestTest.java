package org.jbpm.console.ng.services.client.same;

import java.util.List;

import org.jbpm.console.ng.services.client.api.MessageHolder;
import org.jbpm.console.ng.services.client.api.same.SameApiRequestFactoryImpl;
import org.jbpm.console.ng.services.client.jms.ServiceMessage;
import org.jbpm.console.ng.services.client.jms.ServiceMessage.OperationMessage;
import org.jbpm.console.ng.services.client.jms.ServiceRequestFactoryProvider;
import org.jbpm.console.ng.services.client.jms.serialization.MessageSerializationProvider.Type;
import org.junit.Assert;
import org.junit.Test;
import org.kie.api.event.rule.DefaultAgendaEventListener;
import org.kie.api.runtime.KieSession;
import org.kie.internal.task.api.TaskService;

public class RemoteApiRequestTest extends Assert { 

    private SameApiRequestFactoryImpl getConsoleRequestFactory() { 
        SameApiRequestFactoryImpl factory = ServiceRequestFactoryProvider.createNewSameApiInstance();
        factory.setSerialization(Type.MAP_MESSAGE);
        return factory;
    }
    
    @Test
    public void createBasicRequest() { 
       TaskService taskServiceRequest = getConsoleRequestFactory().createConsoleTaskRequest("release", "correl-1");
       
       long taskId = 3;
       String userId = "bob";
       taskServiceRequest.activate(taskId, userId);
       
       ServiceMessage request = ((MessageHolder) taskServiceRequest).getRequest();
       
       assertTrue(request != null); 
      
       // test method name
       OperationMessage operRequest = request.getOperations().get(0);
       assertTrue("activate".equals(operRequest.getMethodName()));
       
       // test args
       Object [] args = operRequest.getArgs();
       assertTrue(args != null && args.length == 2 );
       assertTrue((Long) args[0] == taskId);
       assertTrue(userId.equals(args[1]));
    }
    
    @Test
    public void createKieSessionRequest() { 
       KieSession kieSessionRequest = getConsoleRequestFactory().createConsoleKieSessionRequest("domain");

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
    public void requestOnlyCreatedOnce() { 
        KieSession kieSessionRequest = getConsoleRequestFactory().createConsoleKieSessionRequest("domain");
        
        kieSessionRequest.startProcess("test");
        kieSessionRequest.signalEvent("party-event", null);
        
        TaskService taskServiceRequest = (TaskService) kieSessionRequest;
        String thirdOperSecondArg = "Clavier";
        taskServiceRequest.activate(22, thirdOperSecondArg);
        taskServiceRequest.deleteComment(22, 45);
        
        ServiceMessage request = ((MessageHolder) taskServiceRequest).getRequest();
        List<OperationMessage> operations = request.getOperations();
        
        assertTrue("Expected 4 operations", operations.size() == 4);
        OperationMessage operRequest = operations.get(0);
        String firstMethod = operRequest.getMethodName();
        assertTrue("First method incorrect: " + firstMethod, "startProcess".equals(firstMethod));
        
        operRequest = operations.get(2);
        Object [] args = operRequest.getArgs();
        assertTrue("Expected 2 arguments for third operation, not " + args.length, args.length == 2);
        assertTrue("Expected '" + thirdOperSecondArg + "' as 2nd argument, not " + args[1], thirdOperSecondArg.equals(args[1]));
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void argumentsMustBePrimitives() { 
        KieSession kieSessionRequest = getConsoleRequestFactory().createConsoleKieSessionRequest("domain");
        
        kieSessionRequest.addEventListener(new DefaultAgendaEventListener());
    }
    
}
