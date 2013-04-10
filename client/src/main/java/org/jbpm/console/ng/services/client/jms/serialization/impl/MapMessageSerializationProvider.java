package org.jbpm.console.ng.services.client.jms.serialization.impl;

import java.util.HashMap;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;

import org.jbpm.console.ng.services.client.jms.ServiceClientRequest;
import org.jbpm.console.ng.services.client.jms.ServiceClientResponse;
import org.jbpm.console.ng.services.client.jms.ServiceServerRequest;
import org.jbpm.console.ng.services.client.jms.ServiceClientRequest.OperationRequest;
import org.jbpm.console.ng.services.client.jms.serialization.MessageSerializationProvider;
import org.jbpm.console.ng.services.shared.MapMessageEnum;

public class MapMessageSerializationProvider implements MessageSerializationProvider {

    private Session jmsSession;

    @Override
    // Add throws exception instead of returning null? 
    public Message convertClientRequestToMessage(ServiceClientRequest request) {
        MapMessage message = null;
        String currentKey = null;

        try {
            message = jmsSession.createMapMessage();

            if (request.getDomainName() != null) {
                currentKey = MapMessageEnum.DomainName.toString();
                message.setString(currentKey, request.getDomainName());
            }

            if (request.getKsessionid() != null) {
                currentKey = MapMessageEnum.KieSessionId.toString();
                message.setString(currentKey, request.getKsessionid());
            }

            HashMap<String, Object> operationInfo;
            int i = 0;
            for (OperationRequest operation : request.getOperations()) {
                operationInfo = new HashMap<String, Object>();
                message.setObject(String.valueOf(i++), operationInfo);

                currentKey = MapMessageEnum.MethodName.toString();
                operationInfo.put(currentKey, operation.getMethod().getName());
                currentKey = MapMessageEnum.ObjectClass.toString();
                operationInfo.put(currentKey, operation.getMethod().getDeclaringClass().getSimpleName());

                Object[] args = operation.getArgs();
                if (args != null) {
                    currentKey = MapMessageEnum.NumArguments.toString();
                    operationInfo.put(String.valueOf(currentKey), operation.getArgs().length);

                    for (int a = 0; i < operation.getArgs().length; ++a) {
                        currentKey = Integer.toString(a);
                        message.setObject(currentKey, args[a]);
                    }
                } else {
                    currentKey = MapMessageEnum.NumArguments.toString();
                    message.setInt(currentKey, 0);
                }
            }
        } catch (JMSException e) {
            try {
                Integer.parseInt(currentKey);
                currentKey = "method argument " + currentKey;
            } catch (NumberFormatException nfe) {
                // do nothing
            }
            throw new UnsupportedOperationException("Unable to insert " + currentKey + " into JMS message.", e);
        }

        return message;
    }

    @Override
    public ServiceServerRequest convertMessageToServerRequest(Message msg) {
        // DBG Auto-generated method stub
        return null;
    }

    @Override
    public Message convertServerResponseToMessage(ServiceServerRequest request) {
        // DBG Auto-generated method stub
        return null;
    }

    @Override
    public ServiceClientResponse convertMessageToClientResponse(Message message) {
        // DBG Auto-generated method stub
        return null;
    }

}
