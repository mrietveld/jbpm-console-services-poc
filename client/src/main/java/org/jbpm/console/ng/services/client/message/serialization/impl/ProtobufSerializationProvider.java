package org.jbpm.console.ng.services.client.message.serialization.impl;

import javax.jms.Message;
import javax.jms.Session;

import org.jbpm.console.ng.services.client.message.ServiceMessage;
import org.jbpm.console.ng.services.client.message.serialization.MessageSerializationProvider;

public class ProtobufSerializationProvider implements MessageSerializationProvider {

    /**
     * See {@link Type}.
     */
    private int serializationType = Type.PROTOBUF.getValue();
    
    @Override
    public Message convertServiceMessageToJmsMessage(ServiceMessage request, Session jmsSession) throws Exception {
        Message jmsMsg = jmsSession.createBytesMessage();
        
        jmsMsg.setIntProperty(SERIALIZATION_TYPE_PROPERTY, serializationType);
        
        return jmsMsg;
    }

    @Override
    public ServiceMessage convertJmsMessageToServiceMessage(Message msg) throws Exception {
        ServiceMessage serviceMsg = null;
        
        return serviceMsg;
    }

    @Override
    public int getSerializationType() {
        return serializationType;
    }

}
