package org.jbpm.console.ng.services.client.api.remote;

import java.lang.reflect.Method;

import org.jbpm.console.ng.services.client.jms.AbstractServiceRequestProxy;

class RemoteApiServiceRequestProxy extends AbstractServiceRequestProxy {
    
    public RemoteApiServiceRequestProxy(String domainName, String sessionid) {
        // Message
        super(domainName, sessionid);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = handleRequestHolderMethodsAndUnsupportedMethods(method);
        if( result != null ) { 
            return result;
        }

        this.request.addOperation(method, args);

        return null;
    }
    

}