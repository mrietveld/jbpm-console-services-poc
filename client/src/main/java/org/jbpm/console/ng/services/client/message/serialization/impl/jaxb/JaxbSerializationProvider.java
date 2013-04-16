package org.jbpm.console.ng.services.client.message.serialization.impl.jaxb;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.jbpm.console.ng.services.client.message.ServiceMessage;
import org.jbpm.console.ng.services.client.message.ServiceMessage.OperationMessage;
import org.jbpm.console.ng.services.client.message.serialization.MessageSerializationProvider;
import org.jbpm.console.ng.services.client.message.serialization.impl.jaxb.JaxbMap.JaxbMapEntry;

// TODO: Add object version checking
public class JaxbSerializationProvider implements MessageSerializationProvider {

    /**
     * See {@link Type}
     */
    private int serializationType = Type.JAXB.getValue();
   
    private static Class<?> [] jaxbClasses = { 
                  JaxbArgument.class, JaxbSingleArgument.class, JaxbMap.class,
                  JaxbServiceMessage.class, JaxbOperation.class
        };
    
    @Override
    public Message convertServiceMessageToJmsMessage(ServiceMessage request, Session jmsSession) throws Exception {
        JaxbServiceMessage jaxbRequest = new JaxbServiceMessage(request);
        String requestString = convertJaxbObjectToString(jaxbRequest);

        Message jmsMsg =  jmsSession.createObjectMessage(requestString);
        jmsMsg.setIntProperty(SERIALIZATION_TYPE_PROPERTY, serializationType);
        
        return jmsMsg;
    }
    
    public static String convertJaxbObjectToString(Object object) throws JAXBException {
        Marshaller marshaller = JAXBContext.newInstance(jaxbClasses).createMarshaller();
        StringWriter stringWriter = new StringWriter();
        
        marshaller.marshal(object, stringWriter);
        String output = stringWriter.toString();
        
        return output;
    }
    
    public ServiceMessage convertJaxbRequesMessageToServiceMessage(JaxbServiceMessage jaxbMsg) throws Exception {
        ServiceMessage serviceMsg = new ServiceMessage(jaxbMsg.getDomain());
        for( JaxbOperation jaxbOper : jaxbMsg.getOperations() ) { 
            OperationMessage operMsg = new OperationMessage(jaxbOper.getMethod(), jaxbOper.getServiceType());
            Object [] args = new Object[jaxbOper.args.size()];
            for( JaxbArgument jaxbArg : jaxbOper.getArgs() ) { 
                int i = jaxbArg.getIndex();
                if( jaxbArg instanceof JaxbSingleArgument ) { 
                   JaxbSingleArgument jaxbSingleArg = (JaxbSingleArgument) jaxbArg;     
                   args[i] = jaxbSingleArg.getContent();
                } else if( jaxbArg instanceof JaxbMap ) { 
                   JaxbMap jaxbMap = (JaxbMap) jaxbArg;     
                   Map<String, Object> argMap = new HashMap<String, Object>();
                   for( JaxbMapEntry jaxbEntry : jaxbMap.getEntryList() ) { 
                       argMap.put(jaxbEntry.getKey(), jaxbEntry.getValue());
                   }
                   args[i] = argMap;
                }
            }
        }
        
        return serviceMsg;
    }
    
    public ServiceMessage convertJmsMessageToServiceMessage(Message jmsMsg) throws Exception {
        String msgXmlString = (String) ((ObjectMessage) jmsMsg).getObject();
        JaxbServiceMessage jaxbServiceMsg = (JaxbServiceMessage) convertStringToJaxbObject(msgXmlString);
        ServiceMessage serviceMsg = convertJaxbRequesMessageToServiceMessage(jaxbServiceMsg);
        return serviceMsg;
    }

    public static Object convertStringToJaxbObject(String xmlStr) throws JAXBException {
        Unmarshaller unmarshaller = JAXBContext.newInstance(jaxbClasses).createUnmarshaller();
        ByteArrayInputStream xmlStrInputStream = new ByteArrayInputStream(xmlStr.getBytes());
        
        Object jaxbObj = unmarshaller.unmarshal(xmlStrInputStream);
        
        return jaxbObj;
    }

    @Override
    public int getSerializationType() {
        return serializationType;
    }

}
