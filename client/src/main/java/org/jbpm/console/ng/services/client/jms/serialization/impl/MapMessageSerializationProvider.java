package org.jbpm.console.ng.services.client.jms.serialization.impl;

import java.util.List;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;

import org.jbpm.console.ng.services.client.jms.ServiceMessage;
import org.jbpm.console.ng.services.client.jms.ServiceMessage.OperationMessage;
import org.jbpm.console.ng.services.client.jms.serialization.MessageSerializationProvider;

public class MapMessageSerializationProvider implements MessageSerializationProvider {

    private final static String DOMAIN_NAME     = "d";
    private final static String KIE_SESSION_ID  = "s";
    private final static String SERVICE_TYPE    = "t";
    private final static String NUM_OPERATIONS  = "o";
    private final static String METHOD_NAME     = "m";
    private final static String NUM_ARGUMENTS   = "a";
    private final static String RESULT          = "r";
    
    private Session jmsSession;

    @Override
    // Add throws exception instead of returning null?
    public Message convertServiceMessageToJmsMessage(ServiceMessage request) throws Exception {
        MapMessage message = null;
        String currentKey = null;

        try {
            message = jmsSession.createMapMessage();

            if (request.getDomainName() != null) {
                currentKey = DOMAIN_NAME;
                message.setString(currentKey, request.getDomainName());
            }

            if (request.getSessionId() != null) {
                currentKey = KIE_SESSION_ID;
                message.setString(currentKey, request.getSessionId());
            }

            List<OperationMessage> operations = request.getOperations();
            int numOperations = request.getOperations().size();
            for (int i = 0; i < numOperations; ++i) {
                OperationMessage operation = operations.get(i);
                currentKey = i + ":" + METHOD_NAME;
                message.setString(currentKey, operation.getMethodName());
                currentKey = i + ":" + SERVICE_TYPE;
                message.setInt(currentKey, operation.getServiceType());

                if (operation.isResponse()) {
                   currentKey = i + ":" + RESULT;
                   message.setObject(currentKey, operation.getResult());
                } else {
                    Object[] args = operation.getArgs();
                    if (args != null) {
                        currentKey = i + ":" + NUM_ARGUMENTS;
                        message.setInt(String.valueOf(currentKey), operation.getArgs().length);

                        for (int a = 0; i < operation.getArgs().length; ++a) {
                            currentKey = i + ":" + Integer.toString(a);
                            message.setObject(currentKey, args[a]);
                        }
                    } else {
                        currentKey = NUM_ARGUMENTS;
                        message.setInt(currentKey, 0);
                    }
                }
            }
            message.setInt(NUM_OPERATIONS, numOperations);
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
    public ServiceMessage convertJmsMessageToServiceMessage(Message msg) throws Exception {
        ServiceMessage serviceMsg = new ServiceMessage();
        MapMessage mapMsg = (MapMessage) msg;
        String currentKey = null;

        try {
            String strValue = mapMsg.getStringProperty(DOMAIN_NAME);
            serviceMsg.setDomainName(strValue);
            
            strValue = mapMsg.getStringProperty(KIE_SESSION_ID);
            serviceMsg.setSessionId(strValue);

            int numOperations = mapMsg.getIntProperty(NUM_OPERATIONS);
            for (int i = 0; i < numOperations; ++i) {
                String methodName = mapMsg.getStringProperty(METHOD_NAME);
                int serviceType = mapMsg.getIntProperty(SERVICE_TYPE);
                Object result = mapMsg.getObjectProperty(RESULT);
                boolean isResponse = result != null;
                OperationMessage operation = new OperationMessage(methodName, serviceType, isResponse);
                serviceMsg.addOperation(operation);
               
                int numArgs = mapMsg.getIntProperty(NUM_ARGUMENTS);
                Object [] args = new Object[numArgs];
                for( int a = 0; a < numArgs; ++a ) { 
                    currentKey = i + ":" + a;
                    args[a] = mapMsg.getObject(currentKey);
                }
                operation.setArgs(args);
            }
        } catch (JMSException e) {
            try {
                Integer.parseInt(currentKey);
                currentKey = "method argument " + currentKey;
            } catch (NumberFormatException nfe) {
                // do nothing
            }
            throw new UnsupportedOperationException("Unable to retrieve " + currentKey + " from JMS message.", e);
        }
        
        return serviceMsg;
    }
    

}
