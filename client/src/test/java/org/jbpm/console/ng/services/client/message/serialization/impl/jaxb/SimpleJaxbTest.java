package org.jbpm.console.ng.services.client.message.serialization.impl.jaxb;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.jbpm.console.ng.services.client.api.MessageHolder;
import org.jbpm.console.ng.services.client.api.ServiceRequestFactoryProvider;
import org.jbpm.console.ng.services.client.api.remote.RemoteApiRequestFactoryImpl;
import org.jbpm.console.ng.services.client.api.remote.api.TaskServiceRequest;
import org.jbpm.console.ng.services.client.message.ServiceMessage;
import org.jbpm.console.ng.services.client.message.ServiceMessage.OperationMessage;
import org.jbpm.console.ng.services.client.message.serialization.MessageSerializationProvider.Type;
import org.junit.Test;

public class SimpleJaxbTest {

    @Test
    public void showRequestXML() throws Exception {
        RemoteApiRequestFactoryImpl requestFactory = ServiceRequestFactoryProvider.createNewRemoteApiInstance();
        requestFactory.setSerialization(Type.JAXB);
        
        TaskServiceRequest taskServiceRequest = requestFactory.createTaskRequest("release", "correl-1"); 
        
        taskServiceRequest.activate(1, "Danno");
        taskServiceRequest.claimNextAvailable("Steve", "en-UK");

        ServiceMessage serviceMsg = ((MessageHolder) taskServiceRequest).getRequest();
        JaxbServiceMessage jaxbMsg = new JaxbServiceMessage(serviceMsg);
        
        JAXBContext jaxbCtx = JAXBContext.newInstance(JaxbServiceMessage.class);
        Marshaller marshaller = jaxbCtx.createMarshaller();
        marshaller.marshal(jaxbMsg, System.out);
    }

    @Test
    public void showResponseXML() throws Exception {
        RemoteApiRequestFactoryImpl requestFactory = ServiceRequestFactoryProvider.createNewRemoteApiInstance();
        requestFactory.setSerialization(Type.JAXB);
        
        TaskServiceRequest taskServiceRequest = requestFactory.createTaskRequest("release", "correl-1"); 
        
        taskServiceRequest.activate(1, "Danno");
        taskServiceRequest.claimNextAvailable("Steve", "en-UK");

        ServiceMessage serviceMsg = ((MessageHolder) taskServiceRequest).getRequest();
        
        for(OperationMessage oper : serviceMsg.getOperations()) { 
            oper.setResult("RESULT!");
        }
        JaxbServiceMessage jaxbMsg = new JaxbServiceMessage(serviceMsg);
        
        JAXBContext jaxbCtx = JAXBContext.newInstance(JaxbServiceMessage.class);
        Marshaller marshaller = jaxbCtx.createMarshaller();
        marshaller.marshal(jaxbMsg, System.out);
    }
    
}
