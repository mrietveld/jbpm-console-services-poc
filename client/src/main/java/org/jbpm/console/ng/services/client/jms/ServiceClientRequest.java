package org.jbpm.console.ng.services.client.jms;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.Queue;

public class ServiceClientRequest {

    private Queue<OperationRequest> operations = new LinkedList<ServiceClientRequest.OperationRequest>();
    private String domainName;
    private String sessionid;

    public ServiceClientRequest(String domainName, String sessionId) { 
        this.domainName = domainName;
        this.sessionid = sessionId;
    }
    
    public Queue<OperationRequest> getOperations() {
        return operations;
    }

    public String getDomainName() {
        return domainName;
    }

    public String getKsessionid() {
        return sessionid;
    }
    
    public void addOperation(Method method, Object [] args) { 
        OperationRequest request = new OperationRequest(method, args);
        operations.add(request); 
    }
    
    /**
     * Class containing information for 1 operation (KieSesion or TaskService).
     */
    public static class OperationRequest { 
        private Method method;
        private Object [] args;

        // Constructor for ServiceClientRequest
        OperationRequest(Method method, Object [] args) { 
            this.method = method;
            this.args = args;
        } 
        
        /**
         * Important information in a Method object:
         * - Method.getDeclaringClass(): the class that has this method
         * - Method.getName(): the name of this method
         * - Method.getParameterTypes(): the types (classes) of the arguments, in order
         */
        public Method getMethod() { 
            return this.method;
        }
        
        public Object [] getArgs() { 
           return this.args;
        }
    }


}
