package org.jbpm.console.ng.services.client.api.same;

import java.lang.reflect.Method;

import org.jbpm.console.ng.services.client.api.AbstractServiceRequestProxy;
import org.jbpm.console.ng.services.client.message.serialization.MessageSerializationProvider;

class SameApiServiceRequestProxy extends AbstractServiceRequestProxy {

    static { 
        String [] moreUnsuportedMethods = { 
                "add-more-unsupported-methods-if-needed"
        };
        
        for( String unsupMethod : moreUnsuportedMethods ) { 
            unsupportedMethods.add(unsupMethod);
        }
    }
    
    public SameApiServiceRequestProxy(String domainName, Long sessionid, MessageSerializationProvider serializationProvider) {
        // Message
        super(domainName,sessionid, serializationProvider);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = handleMessageHolderMethodsAndUnsupportedMethods(method, args);
        if( result != null ) { 
            return result;
        }

        // Another way to limit methods: no methods that have complex arguments (non-primitives) supported
        if (args != null) {
            int i = args.length;
            while (--i >= 0) {
                if (args[i] != null && args[i].getClass().getPackage().getName().startsWith("org")) {
                    throw new UnsupportedOperationException("Only primitive parameters are supported! [" + args[i].getClass().getName() + "]" );
                }
            }
        }

        this.request.addOperation(method, args);
        
        return null;
    }
    
}