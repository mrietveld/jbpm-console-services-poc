package org.jbpm.console.ng.services.ejb;

import java.lang.reflect.Method;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.jbpm.console.ng.services.client.message.ServiceMessage;
import org.jbpm.console.ng.services.client.message.ServiceMessage.OperationMessage;
import org.kie.api.runtime.KieSession;
import org.kie.internal.KieInternalServices;
import org.kie.internal.process.CorrelationKeyFactory;
import org.kie.internal.runtime.manager.Context;
import org.kie.internal.runtime.manager.RuntimeEngine;
import org.kie.internal.runtime.manager.RuntimeManager;
import org.kie.internal.runtime.manager.RuntimeManagerCacheEntryPoint;
import org.kie.internal.runtime.manager.context.EmptyContext;
import org.kie.internal.runtime.manager.context.ProcessInstanceIdContext;
import org.kie.internal.task.api.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless
public class ProcessRequestBean {

    private static Logger logger = LoggerFactory.getLogger(ProcessRequestBean.class);

    private static CorrelationKeyFactory keyFactory = KieInternalServices.Factory.get().newCorrelationKeyFactory();

    @Inject
    private RuntimeManagerCacheEntryPoint runtimeManagerCache;

    public OperationMessage doOperation(ServiceMessage request, OperationMessage operation) {
        OperationMessage response = null;
        switch( operation.getServiceType() ) { 
        case ServiceMessage.KIE_SESSION_REQUEST: 
            response = doKieSessionOperation(request, operation);
            break;
        case ServiceMessage.TASK_SERVICE_REQUEST:
            response = doTaskServiceOperation(request, operation);
            break;
        default:
            throw new UnsupportedOperationException("Unknown service type: " + operation.getServiceType());
        }
        
        return response;
    }
    
    private OperationMessage doKieSessionOperation(ServiceMessage request, OperationMessage operation) {
        KieSession kieSession = getRuntimeEngine(request.getDomainName()).getKieSession();

        Object result = invokeMethod(KieSession.class, operation, kieSession);
        if( result != null ) { 
            return new OperationMessage(operation, result);
        }

        return null;
    }
    
    private OperationMessage doTaskServiceOperation(ServiceMessage request, OperationMessage operation ) {
        TaskService taskService = getRuntimeEngine(request.getDomainName()).getTaskService();

        Object result = invokeMethod(TaskService.class, operation, taskService);
        if( result != null ) { 
            return new OperationMessage(operation, result);
        }

        return null;
    }

    private Object invokeMethod(Class<?> serviceClass, OperationMessage request, Object serviceInstance) {
        Object[] args = request.getArgs();
        Class<?>[] paramTypes = new Class<?>[args.length];
        for (int i = 0; i < args.length; ++i) {
            paramTypes[i] = args[i].getClass();
        }

        Object result = null;
        try {
            Method operationMethod = serviceClass.getMethod(request.getMethodName(), paramTypes);
            result = operationMethod.invoke(serviceInstance, args);
        } catch (Exception e ) {
            handlException(request, e);
        }

        return result;
    }

    private void handlException(OperationMessage request, Exception e) {
        String serviceClassName = null;
        switch(request.getServiceType()) { 
        case ServiceMessage.KIE_SESSION_REQUEST:
            serviceClassName = KieSession.class.getName();
            break;
        case ServiceMessage.TASK_SERVICE_REQUEST:
            serviceClassName = TaskService.class.getName();
            break;
        }
        logger.error("Failed to invoke method " + serviceClassName + "." + request.getMethodName(), e);

        // TODO: how to handle the exception? "FAIL" OperationMessage back to sender/requester? 
    }

    /**
     * Retrieves the {@link RuntimeEngine}.
     * @param domainName
     * @return
     */
    protected RuntimeEngine getRuntimeEngine(String domainName) { 
        return getRuntimeEngine(domainName, null);
    }
    
    protected RuntimeEngine getRuntimeEngine(String domainName, Long processInstanceId) { 
        RuntimeManager runtimeManager = ((RuntimeManagerCacheImpl) runtimeManagerCache).getRuntimeManager(domainName);
        Context<?> runtimeContext = getRuntimeManagerContext(processInstanceId);
        return runtimeManager.getRuntimeEngine(runtimeContext);
    }
    
    private Context<?> getRuntimeManagerContext(Long processInstanceId) { 
        Context<?> managerContext;
        
        if( processInstanceId != null ) { 
            managerContext = new ProcessInstanceIdContext(processInstanceId);
        } else {
            managerContext = EmptyContext.get();
        } 
        
        return managerContext;
    }

}
