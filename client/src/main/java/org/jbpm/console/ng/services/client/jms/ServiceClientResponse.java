package org.jbpm.console.ng.services.client.jms;


public class ServiceClientResponse {

    private String methodName;
    private Object response;
    
    public ServiceClientResponse(String methodName, Object response) { 
        this.methodName = methodName;
        this.response = response;
    }
    
    public String getMethodName() { 
        return methodName;
    }
    
    public Object getResponseObject() { 
       return response; 
    }
}
