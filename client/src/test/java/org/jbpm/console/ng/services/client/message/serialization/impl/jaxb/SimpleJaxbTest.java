package org.jbpm.console.ng.services.client.message.serialization.impl.jaxb;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.jbpm.console.ng.services.client.api.MessageHolder;
import org.jbpm.console.ng.services.client.api.ServiceRequestFactoryProvider;
import org.jbpm.console.ng.services.client.api.fluent.FluentApiRequestFactoryImpl;
import org.jbpm.console.ng.services.client.api.fluent.api.FluentKieSessionRequest;
import org.jbpm.console.ng.services.client.api.remote.RemoteApiRequestFactoryImpl;
import org.jbpm.console.ng.services.client.api.remote.api.TaskServiceRequest;
import org.jbpm.console.ng.services.client.message.ServiceMessage;
import org.jbpm.console.ng.services.client.message.ServiceMessage.OperationMessage;
import org.jbpm.console.ng.services.client.message.serialization.MessageSerializationProvider.Type;
import org.junit.Test;

public class SimpleJaxbTest {

    @Test
    public void shouldBeAbleToMarshallSimpleMessage() throws Exception {
        RemoteApiRequestFactoryImpl requestFactory = ServiceRequestFactoryProvider.createNewRemoteApiInstance();
        requestFactory.setSerialization(Type.JAXB);
        
        TaskServiceRequest taskServiceRequest = requestFactory.createTaskRequest("release", 23l); 
        
        taskServiceRequest.activate(1, "Danno");
        taskServiceRequest.claimNextAvailable("Steve", "en-UK");

        ServiceMessage serviceMsg = ((MessageHolder) taskServiceRequest).getRequest();
        JaxbServiceMessage jaxbMsg = new JaxbServiceMessage(serviceMsg);

        // marshall
        String xmlStr = JaxbSerializationProvider.convertJaxbObjectToString(jaxbMsg);
    }

    @Test
    public void shouldBeAbleToMarshallMoreSimpleMessages() throws Exception {
        RemoteApiRequestFactoryImpl requestFactory = ServiceRequestFactoryProvider.createNewRemoteApiInstance();
        requestFactory.setSerialization(Type.JAXB);
        
        TaskServiceRequest taskServiceRequest = requestFactory.createTaskRequest("release");
        
        taskServiceRequest.activate(1, "Danno");
        taskServiceRequest.claimNextAvailable("Steve", "en-UK");

        ServiceMessage serviceMsg = ((MessageHolder) taskServiceRequest).getRequest();
        
        for(OperationMessage oper : serviceMsg.getOperations()) { 
            oper.setResult("RESULT!");
        }
        JaxbServiceMessage jaxbMsg = new JaxbServiceMessage(serviceMsg);
        
        // marshall
        String xmlStr = JaxbSerializationProvider.convertJaxbObjectToString(jaxbMsg);
    }
    
    @Test
    public void shouldBeAbleToMarshall() throws Exception {
        // Factory
        FluentApiRequestFactoryImpl requestFactory = ServiceRequestFactoryProvider.createNewFluentApiInstance();
        requestFactory.setSerialization(Type.JAXB);
        
        FluentKieSessionRequest request = requestFactory.createKieSessionRequest("dimaggio");
        
        // Operation
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("jersey", new Long(5));
        request.startProcess("batter-up", params);

        // Convert to message
        ServiceMessage serviceMsg = ((MessageHolder) request).getRequest();
        JaxbServiceMessage jaxbMsg = new JaxbServiceMessage(serviceMsg);
        
        // marshall
        String xmlStr = JaxbSerializationProvider.convertJaxbObjectToString(jaxbMsg);
    }
}
