package org.jbpm.console.ng.services.client.message.serialization.impl.jaxb;

import java.io.StringWriter;

import javax.jms.Message;
import javax.jms.Session;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.jbpm.console.ng.services.client.message.ServiceMessage;
import org.jbpm.console.ng.services.client.message.serialization.MessageSerializationProvider;

public class JaxbSerializationProvider implements MessageSerializationProvider {

    /**
     * See {@link Type}
     */
    private int serializationType = Type.JAXB.getValue();
            
    @Override
    public Message convertServiceMessageToJmsMessage(ServiceMessage request, Session jmsSession) throws Exception {
        JaxbServiceMessage jaxbRequest = new JaxbServiceMessage(request);
        String requestString = convertJaxbObjectToString(jaxbRequest);

        Message jmsMsg =  jmsSession.createObjectMessage(requestString);
        jmsMsg.setIntProperty(SERIALIZATION_TYPE_PROPERTY, serializationType);
        
        return jmsMsg;
    }
    
    public static String convertJaxbObjectToString(Object object) throws JAXBException {
        Class<?> [] jaxbClasses = { 
                  JaxbArgument.class, JaxbSingleArgument.class, JaxbMap.class,
                  JaxbServiceMessage.class, JaxbOperation.class
        };
        Marshaller marshaller = JAXBContext.newInstance(jaxbClasses).createMarshaller();
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

    @Override
    public int getSerializationType() {
        return serializationType;
    }

}
