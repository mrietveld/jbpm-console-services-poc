package org.jbpm.console.ng.services.client.jms;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Session;

import org.jbpm.console.ng.services.shared.MapMessageEnum;

class ConsoleRequestProxy implements InvocationHandler {

    private boolean methodSet = false;
    private ClientConsoleRequest request;
    private final String domainName;
    private final String sessionId;

    private static Set<String> unsupportedMethods = new HashSet<String>();
    static {
        Method[] objectMethods = Object.class.getMethods();
        for (Method objMethod : objectMethods) {
            unsupportedMethods.add(objMethod.getName());
        }
    }

    ConsoleRequestProxy() { 
        // Factory
        this.sessionId = null;
        this.domainName = null;
    }
    
    // package level constructor
    ConsoleRequestProxy(String domainName, String sessionId) { 
        // Message
        this.domainName = domainName;
        this.sessionId = sessionId;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("getRequest".equals(method.getName()) && args == null) {
            if (!methodSet) {
                throw new IllegalStateException("No request method has been set yet!");
            }
            return this.request;
        } else if("createMessage".equals(method.getName()) && args.length == 1 && args[0] instanceof Session ) { 
            if (!methodSet) {
                throw new IllegalStateException("No request method has been set yet!");
            }
            return translateRequestToMessage(this.request, this.domainName, this.sessionId, (Session) args[0]);
        }
        
        // No object methods (.wait(), .clone(), etc.. ) supported
        if (unsupportedMethods.contains(method.getName())) {
            throw new UnsupportedOperationException(method.getName() + " is unsupported as a request method!");
        }

        // No methods that have complex arguments (non-primitives) supported
        if (args != null) {
            int i = args.length;
            while (--i >= 0) {
                if (args[i] != null && args[i].getClass().getPackage().getName().startsWith("org")) {
                    throw new UnsupportedOperationException("Only primitive parameters are supported! [" + args[i].getClass().getName() + "]" );
                }
            }
        }

        if (methodSet) {
            throw new IllegalStateException("The request method has already been set!");
        }
        methodSet = true;

        this.request = new ClientConsoleRequest(method, args);

        return null;
    }
    
    private static MapMessage translateRequestToMessage(ClientConsoleRequest request, String domainName, String kieSessionId, Session session) throws JMSException {
        MapMessage message = session.createMapMessage();
        
        String currentKey = null;
        try {
            if( domainName != null ) { 
                currentKey = MapMessageEnum.DomainName.toString();
                message.setString(currentKey, domainName);
            }
            
            if( kieSessionId != null ) { 
                currentKey = MapMessageEnum.KieSessionId.toString();
                message.setString(currentKey, kieSessionId);
            }
            
            currentKey = MapMessageEnum.MethodName.toString();
            message.setString(currentKey, request.getMethod().getName());
            currentKey = MapMessageEnum.ObjectClass.toString();
            message.setString(currentKey, request.getMethod().getDeclaringClass().getSimpleName());
            
            Object [] args = request.getArgs();
            if( args != null ) { 
                currentKey = MapMessageEnum.NumArguments.toString();
                message.setInt(currentKey, request.getArgs().length);

                for( int i = 0; i < request.getArgs().length; ++i ) {
                    currentKey = Integer.toString(i);
                    message.setObject(currentKey, args[i]);
                }
            }
            else { 
                currentKey = MapMessageEnum.NumArguments.toString();
                message.setInt(currentKey, 0);
            }
        } catch (JMSException e) {
            try { 
                Integer.parseInt(currentKey);
                currentKey = "method argument " + currentKey;
            } catch(NumberFormatException nfe ) { 
               // do nothing 
            }
            throw new UnsupportedOperationException("Unable to insert " + currentKey + " into JMS message.", e);
        }

        return message;
    }

}