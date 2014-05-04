package org.kie.services.remote.ws.sei.command;

import javax.jws.WebService;

import org.kie.services.client.serialization.jaxb.impl.JaxbCommandsRequest;
import org.kie.services.client.serialization.jaxb.impl.JaxbCommandsResponse;
import org.kie.services.remote.ws.common.KieRemoteWebServiceException;

/**
 * Only used for WSDL generation
 */
@WebService(
        serviceName = "CommandService", 
        portName = "CommandServicePort", 
        name = "CommandService", 
        targetNamespace = CommandWebService.NAMESPACE)
public class CommandWebServiceWsdlGenerationImpl implements CommandWebService {

    @Override
    public JaxbCommandsResponse execute(JaxbCommandsRequest arg0) throws KieRemoteWebServiceException {
        return null;
    }

}