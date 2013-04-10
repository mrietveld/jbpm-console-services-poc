package org.jbpm.console.ng.services.client.same;

import java.lang.reflect.Method;
import java.util.Queue;

import javax.jms.JMSException;
import javax.jms.Message;

import org.jbpm.console.ng.services.client.api.ClientRequestHolder;
import org.jbpm.console.ng.services.client.api.same.jms.SameApiRequestFactoryImpl;
import org.jbpm.console.ng.services.client.jms.ServiceClientRequest;
import org.jbpm.console.ng.services.client.jms.ServiceRequestFactoryProvider;
import org.jbpm.console.ng.services.client.jms.ServiceClientRequest.OperationRequest;
import org.jbpm.console.ng.services.shared.MapMessageEnum;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.kie.api.event.rule.DefaultAgendaEventListener;
import org.kie.api.runtime.KieSession;
import org.kie.internal.task.api.TaskService;

public class RemoteApiRequestTest extends Assert { 

    private SameApiRequestFactoryImpl getConsoleRequestFactory() { 
        return ServiceRequestFactoryProvider.createNewSameApiInstance();
    }
    
    @Test
    public void createBasicRequest() { 
       TaskService taskServiceRequest = getConsoleRequestFactory().createConsoleTaskRequest("release", "correl-1");
       
       long taskId = 3;
       String userId = "bob";
       taskServiceRequest.activate(taskId, userId);
       
       ServiceClientRequest request = ((ClientRequestHolder) taskServiceRequest).getRequest();
       
       assertTrue(request != null); 
      
       // test method name
       OperationRequest operRequest = request.getOperations().poll();
       assertTrue("activate".equals(operRequest.getMethod().getName()));
       
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
       
       ServiceClientRequest request = ((ClientRequestHolder) kieSessionRequest).getRequest();
       
       assertTrue(request != null); 
      
       // test method name
       OperationRequest operRequest = request.getOperations().poll();
       assertTrue("startProcess".equals(operRequest.getMethod().getName()));
       
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
        
        ServiceClientRequest request = ((ClientRequestHolder) taskServiceRequest).getRequest();
        Queue<OperationRequest> operations = request.getOperations();
        
        assertTrue("Expected 4 operations", operations.size() == 4);
        OperationRequest operRequest = operations.poll();
        Method firstMethod = operRequest.getMethod();
        assertTrue("First method incorrect: " + firstMethod.getName(), "startProcess".equals(firstMethod.getName()));
        
        operations.poll();
        operRequest = operations.poll();
        Object [] args = operRequest.getArgs();
        assertTrue("Expected 2 arguments for third operation, not " + args.length, args.length == 2);
        assertTrue("Expected '" + thirdOperSecondArg + "' as 2nd argument, not " + args[1], thirdOperSecondArg.equals(args[1]));
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void argumentsMustBePrimitives() { 
        KieSession kieSessionRequest = getConsoleRequestFactory().createConsoleKieSessionRequest("domain");
        
        kieSessionRequest.addEventListener(new DefaultAgendaEventListener());
    }
    
    @Test
    @Ignore
    public void exploreTaskServiceRequests() throws JMSException { 
        String domain = "domain";
       TaskService taskServiceRequest = getConsoleRequestFactory().createConsoleTaskRequest(domain);
       int taskId = 2;
       String from = "bob";
       String to = "mary";
       taskServiceRequest.delegate(taskId, from, to);
       
       // Create a JMS session
       Message jmsMsg = ((ClientRequestHolder) taskServiceRequest).createMessage(null);
       assertNotNull(jmsMsg);
       
       assertTrue(domain.equals(jmsMsg.getStringProperty(MapMessageEnum.DomainName.toString())));
       assertTrue("delegate".equals(jmsMsg.getStringProperty(MapMessageEnum.MethodName.toString())));
       assertTrue( 3 == jmsMsg.getIntProperty(MapMessageEnum.NumArguments.toString()));
    }
    
}
