package org.jbpm.console.ng.services.client.jms;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.jbpm.console.ng.services.client.api.ClientRequestHolder;
import org.jbpm.console.ng.services.client.jms.serialization.MessageSerializationProvider;

public abstract class AbstractServiceRequestProxy implements InvocationHandler {

    protected ServiceClientRequest request;
    protected MessageSerializationProvider serializationProvider;

    protected static Set<String> unsupportedMethods = new HashSet<String>();
    static {
        Method[] objectMethods = Object.class.getMethods();
        for (Method objMethod : objectMethods) {
            unsupportedMethods.add(objMethod.getName());
        }
    }

    protected AbstractServiceRequestProxy() {
        // Factory
        this.request = null;
    }
    
    // package level constructor
    protected AbstractServiceRequestProxy(String domainName, String sessionId) { 
        // Message
        this.request = new ServiceClientRequest(domainName, sessionId);
    }
    
    public abstract Object invoke(Object proxy, Method method, Object[] args) throws Throwable;

    protected Object handleRequestHolderMethodsAndUnsupportedMethods(Method method) {
        if (ClientRequestHolder.class.equals(method.getDeclaringClass())) {
            // ClientRequestHolder.getRequest
            if ("getRequest".equals(method.getName())) {
                return this.request;
                // ClientRequestHolder.getRequest
            } else if ("createMessage".equals(method.getName())) {
                return serializationProvider.convertClientRequestToMessage(request);
            }
        }

        // No object methods (.wait(), .clone(), etc.. ) supported
        if (unsupportedMethods.contains(method.getName())) {
            throw new UnsupportedOperationException(method.getName() + " is unsupported as a request method!");
        }
        
        return null;
    }

}
