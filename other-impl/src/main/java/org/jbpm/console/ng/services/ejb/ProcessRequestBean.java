package org.jbpm.console.ng.services.ejb;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.droolsjbpm.services.api.DomainManagerService;
import org.jbpm.console.ng.services.ProcessRequestException;
import org.jbpm.console.ng.services.client.jms.ServiceRequest;
import org.jbpm.console.ng.services.client.jms.ServiceRequest.OperationRequest;
import org.jbpm.console.ng.services.client.jms.ServiceResponse;
import org.jbpm.console.ng.services.client.jms.ServiceResponse.OperationResponse;
import org.jbpm.console.ng.services.ejb.domain.DomainRuntimeManagerProvider;
import org.kie.api.runtime.KieSession;
import org.kie.internal.KieInternalServices;
import org.kie.internal.process.CorrelationKeyFactory;
import org.kie.internal.runtime.manager.Context;
import org.kie.internal.runtime.manager.RuntimeEngine;
import org.kie.internal.runtime.manager.RuntimeManager;
import org.kie.internal.runtime.manager.context.CorrelationKeyContext;
import org.kie.internal.runtime.manager.context.EmptyContext;
import org.kie.internal.runtime.manager.context.ProcessInstanceIdContext;
import org.kie.internal.task.api.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless(name = "ProcessRequestBean", mappedName = "ProcessRequestBean")
public class ProcessRequestBean {

    private static Logger logger = LoggerFactory.getLogger(ProcessRequestBean.class);

    private static CorrelationKeyFactory keyFactory = KieInternalServices.Factory.get().newCorrelationKeyFactory();

    @Inject
    private DomainRuntimeManagerProvider domainRuntimeManagerProvider;

    public OperationResponse doOperation(ServiceRequest request, OperationRequest operation) throws ProcessRequestException {
        OperationResponse response = null;
        switch( operation.getServiceType() ) { 
        case ServiceRequest.KIE_SESSION_REQUEST: 
            response = doKieSessionOperation(request, operation);
            break;
        case ServiceRequest.TASK_SERVICE_REQUEST:
            response = doTaskServiceOperation(request, operation);
            break;
        default:
            throw new ProcessRequestException("Unknown service type: " + operation.getServiceType());
        }
        
        return response;
    }
    
    public OperationResponse doKieSessionOperation(ServiceRequest request, OperationRequest operation) {
        KieSession kieSession = getRuntime(request.getDomainName(), request.getSessionid()).getKieSession();

        ServiceResponse.OperationResponse operResponse = new OperationResponse(operation);
        operResponse.result = invokeMethod(KieSession.class, operation, kieSession);

        return operResponse;
    }
    
    public OperationResponse doTaskServiceOperation(ServiceRequest request, OperationRequest operation ) {
        TaskService taskService = getRuntime(request.getDomainName(), request.getSessionid()).getTaskService();

        ServiceResponse.OperationResponse operResponse = new OperationResponse(operation);
        operResponse.result = invokeMethod(TaskService.class, operation, taskService);

        return operResponse;
    }

    private Object invokeMethod(Class<?> serviceClass, OperationRequest request, Object serviceInstance) {
        Object[] args = request.getArgs();
        Class<?>[] paramTypes = new Class<?>[args.length];
        for (int i = 0; i < args.length; ++i) {
            paramTypes[i] = args[i].getClass();
        }

        Object result = null;
        try {
            Method taskMethod = serviceClass.getMethod(request.getMethodName(), paramTypes);
            result = taskMethod.invoke(serviceInstance, args);
        } catch (SecurityException se ) {
            handlException(request, se);
        } catch (NoSuchMethodException nsme) {
            handlException(request, nsme);
        } catch (IllegalArgumentException iae) {
            handlException(request, iae);
        } catch (IllegalAccessException iae) {
            handlException(request, iae);
        } catch (InvocationTargetException ite) {
            handlException(request, ite);
        }

        return result;
    }

    private void handlException(OperationRequest request, Exception e) {
        String serviceClassName = null;
        switch(request.getServiceType()) { 
        case ServiceRequest.KIE_SESSION_REQUEST:
            serviceClassName = KieSession.class.getName();
            break;
        case ServiceRequest.TASK_SERVICE_REQUEST:
            serviceClassName = TaskService.class.getName();
            break;
        }
        logger.error("Failed to invoke method " + serviceClassName + "." + request.getMethodName(), e);

        // OCRAM:
        e.printStackTrace();
    }

    /**
     * add when org.kie/org.jbpm dependencies are added
     */
    public RuntimeEngine getRuntime(String domainName, String ksessionId) {
        RuntimeManager runtimeManager = domainRuntimeManagerProvider.getRuntimeManager(domainName);

        Context<?> managerContext;

        if (ksessionId == null) {
            managerContext = EmptyContext.get();
        } else {
            try {
                Long longId = Long.parseLong(ksessionId);
                managerContext = ProcessInstanceIdContext.get(longId);
            } catch (NumberFormatException nfe) {
                managerContext = CorrelationKeyContext.get(keyFactory.newCorrelationKey(ksessionId));
            }
        }

        return runtimeManager.getRuntimeEngine(managerContext);
    }

}
