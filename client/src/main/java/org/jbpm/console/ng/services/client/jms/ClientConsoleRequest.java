package org.jbpm.console.ng.services.client.jms;

import java.lang.reflect.Method;

public class ClientConsoleRequest {

    private Method method;
    private Object[] args;
    
    public ClientConsoleRequest(Method method, Object [] args) { 
        this.method = method;
        if( args == null ) { 
            args = new Object[0];
        }
        this.args = args;
    }
    
    public Method getMethod() { 
        return method;
    }
    
    public Object [] getArgs() { 
       return args; 
    }
}
