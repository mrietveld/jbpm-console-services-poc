package org.jbpm.console.ng.services.client.remote;

import javax.jms.JMSException;
import javax.jms.Message;

import org.jbpm.console.ng.services.client.api.ClientRequestHolder;
import org.jbpm.console.ng.services.client.api.remote.RemoteApiRequestFactoryImpl;
import org.jbpm.console.ng.services.client.api.remote.api.KieSessionRequest;
import org.jbpm.console.ng.services.client.api.remote.api.TaskServiceRequest;
import org.jbpm.console.ng.services.client.jms.ServiceClientRequest;
import org.jbpm.console.ng.services.client.jms.ServiceClientRequest.OperationRequest;
import org.jbpm.console.ng.services.client.jms.ServiceRequestFactoryProvider;
import org.jbpm.console.ng.services.shared.MapMessageEnum;
import org.junit.Assert;
import org.junit.Test;

public class ConsoleRequestFactoryTest extends Assert { 

    private RemoteApiRequestFactoryImpl getConsoleRequestFactory() { 
        return ServiceRequestFactoryProvider.createNewRemoteApiInstance();
    }
    
    @Test
    public void createBasicRequest() { 
       TaskServiceRequest taskServiceRequest = getConsoleRequestFactory().createConsoleTaskRequest("release", "correl-1");
       
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
       KieSessionRequest kieSessionRequest = getConsoleRequestFactory().createConsoleKieSessionRequest("domain");

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
    
    @Test(expected=IllegalStateException.class)
    public void requestOnlyCreatedOnce() { 
        KieSessionRequest kieSessionRequest = getConsoleRequestFactory().createConsoleKieSessionRequest("domain");
        
        kieSessionRequest.startProcess("test");
        kieSessionRequest.signalEvent("party-event", null);
    }
    
    @Test
    public void exploreTaskServiceRequests() throws JMSException { 
        String domain = "domain";
       TaskServiceRequest taskServiceRequest = getConsoleRequestFactory().createConsoleTaskRequest(domain);
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
