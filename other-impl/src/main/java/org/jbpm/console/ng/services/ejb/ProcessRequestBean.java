package org.jbpm.console.ng.services.ejb;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.jbpm.console.ng.services.client.jms.ServiceMessage;
import org.jbpm.console.ng.services.client.jms.ServiceMessage.OperationMessage;
import org.jbpm.runtime.manager.api.RuntimeManagerCacheEntryPoint;
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
    
    public OperationMessage doKieSessionOperation(ServiceMessage request, OperationMessage operation) {
        KieSession kieSession = getRuntimeEngine(request.getDomainName(), request.getSessionId()).getKieSession();

        Object result = invokeMethod(KieSession.class, operation, kieSession);
        if( result != null ) { 
            return new OperationMessage(operation, result);
        }

        return null;
    }
    
    public OperationMessage doTaskServiceOperation(ServiceMessage request, OperationMessage operation ) {
        TaskService taskService = getRuntimeEngine(request.getDomainName(), request.getSessionId()).getTaskService();

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

        // OCRAM:
        e.printStackTrace();
    }

    /**
     * Retrieves the correct {@link RuntimeEngine}. <ul>
     * <li>If the sessionId is null, uses an {@link EmptyContext} to get the {@link RuntimeEngine}.</li>
     * <li>If the sessionId is a number, uses a 
     * @param domainName
     * @param ksessionId
     * @return
     */
    protected RuntimeEngine getRuntimeEngine(String domainName, String sessionId) { 
        return getRuntimeEngine(domainName, sessionId, null);
    }
    
    protected RuntimeEngine getRuntimeEngine(String domainName, String sessionId, Long processInstanceId) { 
        RuntimeManager runtimeManager = ((RuntimeManagerCacheImpl) runtimeManagerCache).getRuntimeManager(domainName);
        Context<?> runtimeContext = getRuntimeManagerContext(sessionId, processInstanceId);
        return runtimeManager.getRuntimeEngine(runtimeContext);
    }
    
    private Context<?> getRuntimeManagerContext(String kieSessionId, Long processInstanceId) { 
        Context<?> managerContext;
        
        if( processInstanceId != null ) { 
            managerContext = new ProcessInstanceIdContext(processInstanceId);
        } else if (kieSessionId == null) {
            managerContext = EmptyContext.get();
        } else {
            try {
                Long longId = Long.parseLong(kieSessionId);
                managerContext = ProcessInstanceIdContext.get(longId);
            } catch (NumberFormatException nfe) {
                managerContext = CorrelationKeyContext.get(keyFactory.newCorrelationKey(kieSessionId));
            }
        }
        
        return managerContext;
    }

}
