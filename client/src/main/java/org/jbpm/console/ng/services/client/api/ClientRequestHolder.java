package org.jbpm.console.ng.services.client.api;

import javax.jms.Message;
import javax.jms.Session;

import org.jbpm.console.ng.services.client.jms.ServiceRequest;

public interface ClientRequestHolder {

    public ServiceRequest getRequest();
    
    public Message createMessage(Session session);
    
}
