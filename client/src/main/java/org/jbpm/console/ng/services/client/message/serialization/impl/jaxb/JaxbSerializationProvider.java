package org.jbpm.console.ng.services.client.message.serialization.impl.jaxb;

import java.io.StringWriter;

import javax.jms.Message;
import javax.jms.Session;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.jbpm.console.ng.services.client.message.ServiceMessage;
import org.jbpm.console.ng.services.client.message.serialization.MessageSerializationProvider;

public class JaxbSerializationProvider implements MessageSerializationProvider {

    private Session jmsSession; 
    @Override
    public Message convertServiceMessageToJmsMessage(ServiceMessage request) throws Exception {
        JaxbServiceMessage jaxbRequest = new JaxbServiceMessage(request);
        String requestString = convertJaxbObjectToString(jaxbRequest);

        return jmsSession.createObjectMessage(requestString);
    }
    
    private String convertJaxbObjectToString(Object object) throws Exception {
        Marshaller marshaller = JAXBContext.newInstance(object.getClass()).createMarshaller();
        StringWriter stringWriter = new StringWriter();
        
        marshaller.marshal(object, stringWriter);
        String output = stringWriter.toString();
        
        return output;
    }

    @Override
    public ServiceMessage convertJmsMessageToServiceMessage(Message msg) throws Exception {
        // DBG Auto-generated method stub
        return null;
    }



}
