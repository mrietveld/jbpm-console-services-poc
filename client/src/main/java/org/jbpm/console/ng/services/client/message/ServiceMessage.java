package org.jbpm.console.ng.services.client.message;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ServiceMessage {

    public static transient final int KIE_SESSION_REQUEST = 0;
    public static transient final int TASK_SERVICE_REQUEST = 1;
    
    private String domainName;
    private String sessionId;
    
    private List<OperationMessage> operations = new ArrayList<ServiceMessage.OperationMessage>();

    public ServiceMessage() { 
        // default constructor
    }
    
    public ServiceMessage(ServiceMessage oldRequest) { 
        this.domainName = oldRequest.domainName;
        this.sessionId = oldRequest.sessionId;
    }
    
    public ServiceMessage(String domainName, String sessionId) { 
        this.domainName = domainName;
        this.sessionId = sessionId;
    }
    
    public List<OperationMessage> getOperations() {
        return operations;
    }


    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setSessionId(String sessionid) {
        this.sessionId = sessionid;
    }

    public String getSessionId() {
        return sessionId;
    }
    
    public void addOperation(Method method, Object [] args) { 
        OperationMessage request = new OperationMessage(method, args);
        operations.add(request); 
    }
    
    public void addOperation(OperationMessage operation) {
        if( operation != null ) { 
            operations.add(operation);
        }
    }
    
    /**
     * Class containing information for 1 operation (KieSesion or TaskService).
     */
    public static class OperationMessage { 

        private boolean response = false;
        private String methodName;
        private int serviceType;
        private Long processInstance;
        private Object [] args;

        public OperationMessage(String methodName, int serviceType, boolean response) { 
            this.methodName = methodName;
            this.serviceType = serviceType;
            this.response = response;
        }

        /**
         * Default constructor for REST operations
         * @param methodName
         * @param serviceType
         */
        public OperationMessage(String methodName, int serviceType) { 
            this(methodName.toLowerCase(), serviceType, false);
        }
        
        /**
         * Constructor when replying to a request
         * @param operationRequest
         * @param result
         */
        public OperationMessage(OperationMessage operationRequest, Object result) { 
            this(operationRequest.methodName, operationRequest.serviceType, true);
            this.args = new Object[1];
            this.args[0] = result;
        }
        
        // Constructor for Request
        OperationMessage(Method method, Object [] args) { 
            this.methodName = method.getName().toLowerCase();
            this.args = args;
            setServiceType(method);
        } 
        
        // Constructor for Response
        OperationMessage(Method method, Object result) { 
            this.methodName = method.getName();
            this.args = new Object[1];
            this.args[0] = result;
            this.response = true;
            setServiceType(method);
        } 
        
        private void setServiceType(Method method) { 
            String serviceSimpleName = method.getDeclaringClass().getSimpleName();
            if( serviceSimpleName.toLowerCase().matches("kiesession.*") ) { 
                this.serviceType = KIE_SESSION_REQUEST;
            } else if( serviceSimpleName.toLowerCase().matches("taskservice.*") ) { 
                this.serviceType = TASK_SERVICE_REQUEST;
            } else { 
                throw new UnsupportedOperationException("Unsupported service class: " + serviceSimpleName );
            }
        }
        
        public void setMethodName(String operationMethod) { 
            this.methodName = operationMethod;
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
        
        public void setArgs(Object [] args) { 
           this.args = args;
        }
        
        public Object getResult() { 
           return this.args[0];
        }

        public void setResult(Object result) { 
           this.args = new Object [1];
           this.args[0] = result;
           this.response = true;
        }

        public boolean isResponse() {
            return response;
        }

        public Long getProcessInstance() {
            return processInstance;
        }

        public void setProcessInstance(Long processInstance) {
            this.processInstance = processInstance;
        }

    }


}
