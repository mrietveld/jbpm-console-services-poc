package org.jbpm.console.ng.services.jms;

import java.util.ArrayList;
import java.util.List;

import javax.jms.Queue;


public class ServerConsoleRequest {

    public String domainName = null;
    public String kieSessionid = null;
    
    public String objectClass = null;
    public String methodName = null;
    
    public int numArguments = 0;
   
    public List<Object> args = new ArrayList<Object>();
    
    public Queue replyToQueue;
    
}
