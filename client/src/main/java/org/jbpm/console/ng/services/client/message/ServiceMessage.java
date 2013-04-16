package org.jbpm.console.ng.services.client.message;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.kie.api.runtime.KieSession;

public class ServiceMessage {

    public static transient final int KIE_SESSION_REQUEST = 0;
    public static transient final int TASK_SERVICE_REQUEST = 1;

    private final static int version = 1;
    private String domainName;
    
    private List<OperationMessage> operations = new ArrayList<ServiceMessage.OperationMessage>();

    public ServiceMessage() { 
        // default constructor
    }
    
    public ServiceMessage(String domainName) { 
        this.domainName = domainName;
    }
    
    public List<OperationMessage> getOperations() {
        return operations;
    }

    public int getVersion() {
        return version;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getDomainName() {
        return domainName;
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
        private Long objectId;
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
        
        @SuppressWarnings("rawtypes")
        private void setServiceType(Method method) { 
            Class serviceClass = method.getDeclaringClass();
            this.serviceType = determineServiceType(serviceClass);
            
            if( this.serviceType == -1 ) { 
                // Only necessary if we use the "Same" API
              
                Queue<Class<?>> interfaces = new LinkedList(Arrays.asList(KieSession.class.getInterfaces()));
                while( ! interfaces.isEmpty() ) {
                    Class<?> inter = interfaces.poll();
                    if( serviceClass.equals(inter) ) { 
                        this.serviceType = KIE_SESSION_REQUEST;
                        return;
                    }
                    interfaces.addAll(Arrays.asList(inter.getInterfaces()));
                }

                // TaskService does not extend (nor is extended by) any other classes
                
                throw new UnsupportedOperationException("Unsupported service class: " + serviceClass.getCanonicalName() );
            }
        }
        
        private int determineServiceType(Class serviceClass) { 
            if( serviceClass.getSimpleName().toLowerCase().matches(".*kiesession.*") ) { 
                return KIE_SESSION_REQUEST;
            } else if( serviceClass.getSimpleName().toLowerCase().matches(".*taskservice.*") ) { 
                return TASK_SERVICE_REQUEST;
            } else { 
                return -1;
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

        public Long getObjectId() {
            return objectId;
        }

        public void setObjectId(Long processInstanceOrTaskId) {
            this.objectId = processInstanceOrTaskId;
        }

    }


}
