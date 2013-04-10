package org.jbpm.console.ng.services.client.jms.serialization;

import javax.jms.Message;

import org.jbpm.console.ng.services.client.jms.ServiceClientRequest;
import org.jbpm.console.ng.services.client.jms.ServiceClientResponse;
import org.jbpm.console.ng.services.client.jms.ServiceServerRequest;

public interface MessageSerializationProvider {

    public Message convertClientRequestToMessage(ServiceClientRequest request);
    
    public ServiceServerRequest convertMessageToServerRequest(Message msg);
    
    public Message convertServerResponseToMessage(ServiceServerRequest request);
    
    public ServiceClientResponse convertMessageToClientResponse(Message message);
    
}
