package org.jbpm.console.ng.services.client.fluent;

import java.util.HashMap;

import org.jbpm.console.ng.services.client.api.MessageHolder;
import org.jbpm.console.ng.services.client.api.ServiceRequestFactoryProvider;
import org.jbpm.console.ng.services.client.api.fluent.FluentApiRequestFactoryImpl;
import org.jbpm.console.ng.services.client.api.fluent.api.FluentKieSessionRequest;
import org.jbpm.console.ng.services.client.api.fluent.api.FluentTaskServiceRequest;
import org.jbpm.console.ng.services.client.message.ServiceMessage;
import org.jbpm.console.ng.services.client.message.ServiceMessage.OperationMessage;
import org.jbpm.console.ng.services.client.message.serialization.MessageSerializationProvider.Type;
import org.junit.Assert;
import org.junit.Test;

public class FluentApiRequestTest extends Assert { 

    private FluentApiRequestFactoryImpl getFluentApiRequestFactory() { 
        FluentApiRequestFactoryImpl factory = ServiceRequestFactoryProvider.createNewFluentApiInstance();
        factory.setSerialization(Type.MAP_MESSAGE);
        return factory;
    }
    
    @Test
    public void basicRequestShouldBeCreatedAndContainInfo() { 
       FluentTaskServiceRequest taskServiceRequest = getFluentApiRequestFactory().createTaskServiceRequest("release", 42l);
       
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
       FluentKieSessionRequest kieSessionRequest = getFluentApiRequestFactory().createKieSessionRequest("domain");

       String processName = "example-process";
       kieSessionRequest.startProcess("example-process");
       
       ServiceMessage request = ((MessageHolder) kieSessionRequest).getRequest();
       
       assertTrue(request != null); 
      
       // test method name
       OperationMessage operRequest = request.getOperations().get(0);
       assertEquals("startProcess".toLowerCase(), operRequest.getMethodName());
       
       // test args
       Object [] args = operRequest.getArgs();
       assertTrue(args != null && args.length == 1 );
       assertTrue(processName.equals(args[0]));
    }
    
    @Test
    public void shouldBeAbleToAddMultipleOperations() { 
        FluentKieSessionRequest kieSessionRequest = getFluentApiRequestFactory().createKieSessionRequest("domain");
        
        kieSessionRequest.startProcess("test").signalEvent("party-event", null);
    }
    
    @Test
    public void shouldBeAbleToCallMethodWithParameters() { 
        FluentKieSessionRequest kieSessionRequest = getFluentApiRequestFactory().createKieSessionRequest("domain");

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("user-id", "Lin Dze");
        kieSessionRequest.startProcess("test-process", params);
       
        String msgXmlString = ((MessageHolder) kieSessionRequest).getMessageJaxbXml();
    }
}
