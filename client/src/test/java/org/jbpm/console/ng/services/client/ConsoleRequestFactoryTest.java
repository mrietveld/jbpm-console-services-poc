package org.jbpm.console.ng.services.client;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.hornetq.jms.client.HornetQMapMessage;
import org.jbpm.console.ng.services.client.jms.ClientConsoleRequest;
import org.jbpm.console.ng.services.client.jms.ConsoleRequestFactory;
import org.jbpm.console.ng.services.client.jms.ConsoleRequestHolder;
import org.jbpm.console.ng.services.shared.MapMessageEnum;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.kie.api.event.rule.DefaultAgendaEventListener;
import org.kie.api.runtime.KieSession;
import org.kie.internal.task.api.TaskService;

public class ConsoleRequestFactoryTest extends Assert { 

    private ConsoleRequestFactory getConsoleRequestFactory() { 
        return ConsoleRequestFactory.createNewInstance();
    }
    
    @Test
    public void createBasicRequest() { 
       TaskService taskServiceRequest = getConsoleRequestFactory().createConsoleTaskRequest("release", "correl-1");
       
       long taskId = 3;
       String userId = "bob";
       taskServiceRequest.activate(taskId, userId);
       
       ClientConsoleRequest request = ((ConsoleRequestHolder) taskServiceRequest).getRequest();
       
       assertTrue(request != null); 
      
       // test method name
       assertTrue("activate".equals(request.getMethod().getName()));
       
       // test args
       Object [] args = request.getArgs();
       assertTrue(args != null && args.length == 2 );
       assertTrue((Long) args[0] == taskId);
       assertTrue(userId.equals(args[1]));
    }
    
    @Test
    public void createKieSessionRequest() { 
       KieSession kieSessionRequest = getConsoleRequestFactory().createConsoleKieSessionRequest("domain");

       String processName = "example-process";
       kieSessionRequest.startProcess("example-process");
       
       ClientConsoleRequest request = ((ConsoleRequestHolder) kieSessionRequest).getRequest();
       
       assertTrue(request != null); 
      
       // test method name
       assertTrue("startProcess".equals(request.getMethod().getName()));
       
       // test args
       Object [] args = request.getArgs();
       assertTrue(args != null && args.length == 1 );
       assertTrue(processName.equals(args[0]));
    }
    
    @Test(expected=IllegalStateException.class)
    public void requestOnlyCreatedOnce() { 
        KieSession kieSessionRequest = getConsoleRequestFactory().createConsoleKieSessionRequest("domain");
        
        kieSessionRequest.startProcess("test");
        kieSessionRequest.signalEvent("party-event", null);
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
       Message jmsMsg = ((ConsoleRequestHolder) taskServiceRequest).createMessage(null);
       assertNotNull(jmsMsg);
       
       assertTrue(domain.equals(jmsMsg.getStringProperty(MapMessageEnum.DomainName.toString())));
       assertTrue("delegate".equals(jmsMsg.getStringProperty(MapMessageEnum.MethodName.toString())));
       assertTrue( 3 == jmsMsg.getIntProperty(MapMessageEnum.NumArguments.toString()));
    }
    
}
