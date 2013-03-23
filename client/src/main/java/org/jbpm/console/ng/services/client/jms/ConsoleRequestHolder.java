package org.jbpm.console.ng.services.client.jms;

import javax.jms.Message;
import javax.jms.Session;

public interface ConsoleRequestHolder {

    public ClientConsoleRequest getRequest();
    
    public Message createMessage(Session session);
    
}
