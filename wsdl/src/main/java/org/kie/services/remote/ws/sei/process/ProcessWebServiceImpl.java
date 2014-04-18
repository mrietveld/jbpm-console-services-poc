package org.kie.services.remote.ws.sei.process;

import javax.jws.WebService;

import org.kie.services.remote.ws.wsdl.VersionSO;

@WebService(
        serviceName = "ProcessService", 
        portName = "ProcessServicePort", 
        name = "ProcessService", 
        targetNamespace = "http://services.remote.kie.org/process")
public class ProcessWebServiceImpl implements ProcessWebService {

    @Override
    public ProcessInstanceInfo start(ProcessDefIdAndParameters procDefIdAndParams) throws ProcessWebServiceException {
        // DBG Auto-generated method stub
        return null;
    }

    @Override
    public void abort(ProcessInstanceId procInstId) throws ProcessWebServiceException {
        // DBG Auto-generated method stub

    }

    @Override
    public void signal(ProcessInstanceId procInstId) throws ProcessWebServiceException {
        // DBG Auto-generated method stub

    }

    @Override
    public ProcessInstanceInfo getInfo(GetProcessInstanceInfo getProcessInstanceInfo) throws ProcessWebServiceException {
        // DBG Auto-generated method stub
        return null;
    }

    @Override
    public void completeWorkItem(WorkItemId workItemId) throws ProcessWebServiceException {
        // DBG Auto-generated method stub

    }

    @Override
    public void abortWorkItem(WorkItemId workItemId) throws ProcessWebServiceException {
        // DBG Auto-generated method stub

    }

    @Override
    public VersionSO version() throws ProcessWebServiceException {
        // DBG Auto-generated method stub
        return null;
    }

}
