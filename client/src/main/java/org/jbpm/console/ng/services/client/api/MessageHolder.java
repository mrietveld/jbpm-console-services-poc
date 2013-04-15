package org.jbpm.console.ng.services.client.api;

import javax.jms.Message;
import javax.jms.Session;

import org.jbpm.console.ng.services.client.jms.ServiceMessage;

public interface MessageHolder {

    public ServiceMessage getRequest();
    
    public Message createMessage(Session session);
    
}
