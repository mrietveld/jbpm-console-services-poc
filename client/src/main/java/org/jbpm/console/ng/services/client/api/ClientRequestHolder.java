package org.jbpm.console.ng.services.client.api;

import javax.jms.Message;
import javax.jms.Session;

import org.jbpm.console.ng.services.client.jms.ServiceClientRequest;

public interface ClientRequestHolder {

    public ServiceClientRequest getRequest();
    
    public Message createMessage(Session session);
    
}
