package org.jbpm.console.ng.services.client.jms.serialization;

import javax.jms.Message;

import org.jbpm.console.ng.services.client.jms.ServiceResponse;
import org.jbpm.console.ng.services.client.jms.ServiceRequest;

public interface MessageSerializationProvider {

    public Message convertClientRequestToMessage(ServiceRequest request);
    
    public ServiceRequest convertMessageToServerRequest(Message msg);
    
    public Message convertResponseToMessage(ServiceResponse response);
    
    public ServiceResponse convertMessageToClientResponse(Message message);
    
}
