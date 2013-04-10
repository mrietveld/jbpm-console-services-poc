package org.jbpm.console.ng.services.client.api.same.jms;

import java.lang.reflect.Method;

import org.jbpm.console.ng.services.client.jms.AbstractServiceRequestProxy;

class SameApiRequestProxy extends AbstractServiceRequestProxy {

    static { 
        String [] moreUnsuportedMethods = { 
                ""
        };
        
        for( String unsupMethod : moreUnsuportedMethods ) { 
            unsupportedMethods.add(unsupMethod);
        }
    }
    
    public SameApiRequestProxy(String domainName, String sessionid) {
        // Message
        super(domainName,sessionid);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = handleRequestHolderMethodsAndUnsupportedMethods(method);
        if( result != null ) { 
            return result;
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

        this.request.addOperation(method, args);
        
        return null;
    }
    
}