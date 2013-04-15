package org.jbpm.console.ng.services.client.jms.serialization.impl.jaxb;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.jbpm.console.ng.services.client.api.MessageHolder;
import org.jbpm.console.ng.services.client.api.remote.RemoteApiRequestFactoryImpl;
import org.jbpm.console.ng.services.client.api.remote.api.TaskServiceRequest;
import org.jbpm.console.ng.services.client.jms.ServiceMessage;
import org.jbpm.console.ng.services.client.jms.ServiceMessage.OperationMessage;
import org.jbpm.console.ng.services.client.jms.ServiceRequestFactoryProvider;
import org.jbpm.console.ng.services.client.jms.serialization.MessageSerializationProvider.Type;
import org.junit.Test;
import org.kie.api.runtime.KieSession;

public class SimpleJaxbTest {

    @Test
    public void showRequestXML() throws Exception {
        RemoteApiRequestFactoryImpl requestFactory = ServiceRequestFactoryProvider.createNewRemoteApiInstance();
        requestFactory.setSerialization(Type.JAXB);
        
        TaskServiceRequest taskServiceRequest = requestFactory.createConsoleTaskRequest("release", "correl-1"); 
        
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
        
        TaskServiceRequest taskServiceRequest = requestFactory.createConsoleTaskRequest("release", "correl-1"); 
        
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
    
    private byte[] convertToByteArray(Object input) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oout = new ObjectOutputStream(baos);
        oout.writeObject(input);
        return baos.toByteArray();
    }
}
