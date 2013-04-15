package org.jbpm.console.ng.services.client.message.serialization.impl;

import javax.jms.Message;
import javax.jms.Session;

import org.jbpm.console.ng.services.client.message.ServiceMessage;
import org.jbpm.console.ng.services.client.message.serialization.MessageSerializationProvider;

public class ProtobufSerializationProvider implements MessageSerializationProvider {

    @Override
    public Message convertServiceMessageToJmsMessage(ServiceMessage request, Session jmsSession) throws Exception {
        // DBG Auto-generated method stub
        return null;
    }

    @Override
    public ServiceMessage convertJmsMessageToServiceMessage(Message msg) throws Exception {
        // DBG Auto-generated method stub
        return null;
    }

}
