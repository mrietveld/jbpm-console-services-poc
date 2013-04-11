package org.jbpm.console.ng.services.client.jms;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.Queue;

public class ServiceRequest {

    public static transient final int KIE_SESSION_REQUEST = 0;
    public static transient final int TASK_SERVICE_REQUEST = 1;
    
    private String domainName;
    private String sessionid;
    
    private Queue<OperationRequest> operations = new LinkedList<ServiceRequest.OperationRequest>();

    public ServiceRequest() { 
        // default constructor
    }
    
    public ServiceRequest(String domainName, String sessionId) { 
        this.domainName = domainName;
        this.sessionid = sessionId;
    }
    
    public Queue<OperationRequest> getOperations() {
        return operations;
    }


    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public String getSessionid() {
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
        private String methodName;
        private int serviceType;
        private Object [] args;

        // Constructor for ServiceClientRequest
        OperationRequest(Method method, Object [] args) { 
            this.methodName = method.getName();
            this.args = args;
            
            String serviceSimpleName = method.getDeclaringClass().getSimpleName();
            if( serviceSimpleName.toLowerCase().matches("kiesession") ) { 
                this.serviceType = KIE_SESSION_REQUEST;
            } else if( serviceSimpleName.toLowerCase().matches("taskservice") ) { 
                this.serviceType = TASK_SERVICE_REQUEST;
            } else { 
                throw new UnsupportedOperationException("Unsupported service class: " + method.getDeclaringClass().getName() );
            }
        } 
        
        public String getMethodName() { 
            return this.methodName;
        }
        
        public int getServiceType() { 
            return this.serviceType;
        }
        
        public Object [] getArgs() { 
           return this.args;
        }
        
    }


}
