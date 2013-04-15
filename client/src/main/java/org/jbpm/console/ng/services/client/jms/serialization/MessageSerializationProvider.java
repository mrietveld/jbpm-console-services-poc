package org.jbpm.console.ng.services.client.jms.serialization;

import javax.jms.Message;

import org.jbpm.console.ng.services.client.jms.ServiceMessage;

public interface MessageSerializationProvider {

    public Message convertServiceMessageToJmsMessage(ServiceMessage request) throws Exception;
    
    public ServiceMessage convertJmsMessageToServiceMessage(Message msg) throws Exception;
    
    public enum Type { 
        MAP_MESSAGE, JAXB, PROTOBUF, OTHER;
    }
}
