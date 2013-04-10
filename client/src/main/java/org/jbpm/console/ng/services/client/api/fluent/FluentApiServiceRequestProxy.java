package org.jbpm.console.ng.services.client.api.fluent;

import java.lang.reflect.Method;

import org.jbpm.console.ng.services.client.jms.AbstractServiceRequestProxy;

class FluentApiServiceRequestProxy extends AbstractServiceRequestProxy {

    FluentApiServiceRequestProxy(String domainName, String sessionId) { 
        // Message
        super(domainName,sessionId);
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = handleRequestHolderMethodsAndUnsupportedMethods(method);
        if( result != null ) { 
            return result;
        }
        
        this.request.addOperation(method, args);
        
        return this;
    }
   
    

}