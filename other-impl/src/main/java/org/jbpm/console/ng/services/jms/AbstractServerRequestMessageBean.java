package org.jbpm.console.ng.services.jms;

import javax.jms.JMSException;
import javax.jms.MapMessage;

import org.jbpm.console.ng.services.ejb.ServerConsoleRequest;
import org.jbpm.console.ng.services.shared.MapMessageEnum;

public class AbstractServerRequestMessageBean {

//    private class Logger logger;
    
    protected ServerConsoleRequest mapMapMessageToServerRequest(MapMessage mapMessage) {
        ServerConsoleRequest request = new ServerConsoleRequest();

        MapMessageEnum[] stringValueKeys = { MapMessageEnum.DomainName, MapMessageEnum.KieSessionId, MapMessageEnum.ObjectClass,
                MapMessageEnum.MethodName };
        for (MapMessageEnum key : stringValueKeys) {
            String value = (String) getValue(key.toString(), mapMessage, MapValueType.STRING);
            switch (key) {
            case DomainName:
                request.domainName = value;
                break;
            case KieSessionId:
                request.kieSessionid = value;
                break;
            case ObjectClass:
                request.objectClass = value;
                break;
            case MethodName:
                request.methodName = value;
                break;
            default:
                throw new IllegalStateException("Unexpected key in JMS message: " + key.toString());
            }
        }

        MapMessageEnum key = MapMessageEnum.NumArguments;
        int numArgs = (Integer) getValue(key.toString(), mapMessage, MapValueType.OBJECT);
        request.numArguments = numArgs;

        for (int i = 0; i < numArgs; ++i) {
            Object value = getValue(String.valueOf(i), mapMessage, MapValueType.OBJECT);
            request.args.add(value);
        }

        return request;
    }

    private Object getValue(String key, MapMessage mapMsg, MapValueType type) {
        Object result = null;
        try {
            switch (type) {
            case STRING:
                result = mapMsg.getString(key);
                break;
            case INT:
                result = mapMsg.getInt(key);
                break;
            case OBJECT:
                result = mapMsg.getObject(key);
                break;
            default:
                throw new IllegalStateException("Unknown MapMessage value type: " + type.toString());
            }
        } catch (JMSException jmse) {
            // log
        }
        return result;
    }

    enum MapValueType {
        STRING, INT, OBJECT;
    }

}
