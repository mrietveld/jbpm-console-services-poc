package org.jbpm.console.ng.services.ejb;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSException;

import org.jbpm.console.ng.services.domain.DomainRuntimeManagerProvider;
import org.jbpm.console.ng.services.shared.MapMessageEnum;
import org.kie.api.runtime.KieSession;
import org.kie.internal.KieInternalServices;
import org.kie.internal.process.CorrelationKeyFactory;
import org.kie.internal.runtime.manager.Context;
import org.kie.internal.runtime.manager.Runtime;
import org.kie.internal.runtime.manager.RuntimeManager;
import org.kie.internal.runtime.manager.context.CorrelationKeyContext;
import org.kie.internal.runtime.manager.context.EmptyContext;
import org.kie.internal.runtime.manager.context.ProcessInstanceIdContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Stateless(name = "ProcessRequestBean", mappedName = "ProcessRequestBean")
public class ProcessRequestBean {

    private static Logger logger = LoggerFactory.getLogger(ProcessRequestBean.class);
           
    private static CorrelationKeyFactory keyFactory = KieInternalServices.Factory.get().newCorrelationKeyFactory();
            
    @Inject
    private DomainRuntimeManagerProvider runtimeManagerProvider;
    
    public Object doKieSessionOperation(ServerConsoleRequest request) {
        KieSession kieSession = getRuntime(request.domainName, request.kieSessionid).getKieSession();
        return invokeMethod(KieSession.class, request, kieSession);
    }

    private Object invokeMethod(Class<?> serviceClass, ServerConsoleRequest request, Object serviceObject) { 
        Class<?> [] paramTypes = new Class<?> [request.numArguments];
        for( int i = 0; i <request.args.size(); ++i ) { 
           paramTypes[i]  = request.args.get(i).getClass();
        }
        
        Object result = null;
        try {
            Method taskMethod = serviceClass.getMethod(request.methodName, paramTypes);
            result = taskMethod.invoke(serviceObject, request.args);
        } catch (SecurityException se) {
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
    
    private void handlException(ServerConsoleRequest request, Exception e) { 
        logger.error("Failed to invoke method " + request.objectClass + "." + request.methodName, e);
        e.printStackTrace();
    }
    
    /** 
     * add when org.kie/org.jbpm dependencies are added
     */
    public Runtime getRuntime(String domainName, String ksessionId) {
        RuntimeManager runtimeManager = runtimeManagerProvider.getRuntimeManager(domainName);

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
        
        return runtimeManager.getRuntime(managerContext);
    }

}
