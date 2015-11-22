package org.kie.services.remote.ws.sei.task;

import javax.jws.WebService;

import org.kie.services.remote.ws.common.KieRemoteWebServiceException;

/**
 * Only used for WSDL generation
 */
@WebService(
        serviceName = "TaskService", 
        portName = "TaskServicePort", 
        name = "TaskService", 
        targetNamespace = TaskWebService.NAMESPACE)
public class TaskWebServiceWsdlGenerationImpl implements TaskWebService {

    @Override
    public void taskOperation(TaskOperationRequest arg0) throws KieRemoteWebServiceException {
    }

    @Override
    public TaskQueryResponse query(TaskQueryRequest arg0) throws KieRemoteWebServiceException {
        return null;
    }

}