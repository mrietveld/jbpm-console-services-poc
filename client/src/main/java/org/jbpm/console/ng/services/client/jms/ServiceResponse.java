package org.jbpm.console.ng.services.client.jms;

import java.util.LinkedList;
import java.util.Queue;

import org.jbpm.console.ng.services.client.jms.ServiceRequest.OperationRequest;

public class ServiceResponse {

    private final String domainName;
    private final String sessionId;

    private final Queue<OperationResponse> responses = new LinkedList<ServiceResponse.OperationResponse>();

    public ServiceResponse(ServiceRequest request) { 
        this.domainName = request.getDomainName();
        this.sessionId = request.getSessionid();
    }
    
    public ServiceResponse(String domainName, String kieSessionId) { 
       this.domainName = domainName;
       this.sessionId = kieSessionId;
    }
    
    public String getDomainName() {
        return domainName;
    }

    public String getSessionId() {
        return sessionId;
    }

    public Queue<OperationResponse> getResponses() {
        return responses;
    }

    public static class OperationResponse {
        public final int serviceType;
        public final String methodName;

        public Object result = null;
        
        public OperationResponse(OperationRequest request) { 
            this.serviceType = request.getServiceType();
            this.methodName = request.getMethodName();
        }
        
        public OperationResponse(int serviceType, String method) { 
            this.serviceType = serviceType;
            this.methodName = method;
        }

        public int getServiceType() {
            return serviceType;
        }

        public String getMethodName() {
            return methodName;
        }

        public Object getResult() {
            return result;
        }

        public void setResult(Object response) {
            this.result = response;
        }
    }
}
