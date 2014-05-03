package org.kie.services.remote.ws.sei.process;

import javax.jws.WebService;

import org.kie.services.remote.ws.objects.KieRemoteWebServiceException;

/**
 * Only used for WSDL generation
 */
@WebService(
        serviceName = "ProcessService", 
        portName = "ProcessServicePort", 
        name = "ProcessService", 
        targetNamespace = ProcessWebService.NAMESPACE)
public class ProcessWebServiceWsdlGenerationImpl implements ProcessWebService {

    @Override
    public ProcessInstanceInfo startProcess(ProcessDefIdAndParameters procDefIdAndParams) throws KieRemoteWebServiceException {
        return null;
    }

    @Override
    public void abortProcess(ProcessInstanceId procInstId) throws KieRemoteWebServiceException { }

    @Override
    public void signalProcess(ProcessInstanceIdAndSignal procInstIdAndSignal) throws KieRemoteWebServiceException { }

    @Override
    public ProcessInstanceInfo getProcessInstanceInfo(ProcessInstanceInfoRequest getProcessInstanceInfo) throws KieRemoteWebServiceException {
        return null;
    }

    @Override
    public void completeWorkItem(WorkItemId workItemId) throws KieRemoteWebServiceException { }

    @Override
    public void abortWorkItem(WorkItemId workItemId) throws KieRemoteWebServiceException { }

}
