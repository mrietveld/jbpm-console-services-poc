package org.jbpm.console.ng.services.client.jms;

import javax.jms.Queue;


public class ServiceServerResponse {

    public Object responseObject = null;
    
    public String kieSessionid = null;
    public String serviceClass = null;
    public String methodName = null;
    
    public Queue replyToQueue;
    
}
