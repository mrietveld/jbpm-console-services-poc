package org.jbpm.console.ng.services.client.api;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.jbpm.console.ng.services.client.message.ServiceMessage;
import org.jbpm.console.ng.services.client.message.serialization.MessageSerializationProvider;

public abstract class AbstractServiceRequestProxy implements InvocationHandler {

    protected ServiceMessage request;
    protected final MessageSerializationProvider serializationProvider;

    protected static Set<String> unsupportedMethods = new HashSet<String>();
    static {
        Method[] objectMethods = Object.class.getMethods();
        for (Method objMethod : objectMethods) {
            unsupportedMethods.add(objMethod.getName());
        }
    }

    // package level constructor
    protected AbstractServiceRequestProxy(String domainName, String sessionId, MessageSerializationProvider serializationProvider) { 
        // Message
        this.request = new ServiceMessage(domainName, sessionId);
        this.serializationProvider = serializationProvider;
    }
    
    public abstract Object invoke(Object proxy, Method method, Object[] args) throws Throwable;

    protected Object handleRequestHolderMethodsAndUnsupportedMethods(Method method) {
        if (MessageHolder.class.equals(method.getDeclaringClass())) {
            // ClientRequestHolder.getRequest
            if ("getRequest".equals(method.getName())) {
                return this.request;
                // ClientRequestHolder.getRequest
            } else if ("createMessage".equals(method.getName())) {
                try { 
                    ServiceMessage request = this.request;
                    this.request = new ServiceMessage(this.request);
                    return serializationProvider.convertServiceMessageToJmsMessage(request);
                } catch( Exception e ) { 
                    throw new RuntimeException("Unable to convert request to message: " + e.getMessage(), e);
                }
            }
        }

        // No object methods (.wait(), .clone(), etc.. ) supported
        if (unsupportedMethods.contains(method.getName())) {
            throw new UnsupportedOperationException(method.getName() + " is unsupported as a request method!");
        }
        
        return null;
    }

}
