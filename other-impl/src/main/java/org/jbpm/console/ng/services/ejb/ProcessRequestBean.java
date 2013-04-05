package org.jbpm.console.ng.services.ejb;


import java.util.HashMap;

import javax.ejb.Stateless;
import javax.jms.JMSException;
import javax.jms.MapMessage;

import org.jbpm.console.ng.services.shared.MapMessageEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Stateless(name = "ProcessRequestBean", mappedName = "ProcessRequestBean")
public class ProcessRequestBean {

    private static Logger logger = LoggerFactory.getLogger(ProcessRequestBean.class);
            
    
    public void doTaskServiceOperation(MapMessage requestMap) throws JMSException {
        String domainName = (String) requestMap.getString(MapMessageEnum.DomainName.toString());
        String kieSessionid = (String) requestMap.getString(MapMessageEnum.KieSessionId.toString());
        String objectClass = (String) requestMap.getString(MapMessageEnum.ObjectClass.toString());
        String methodName = (String) requestMap.getString(MapMessageEnum.MethodName.toString());
        
        if( true ) { 
            System.out.println(domainName + ":" + objectClass + "." + methodName);
        }
        else { 
//            TaskService taskService = getRuntime(domainName, kieSessionid).getTaskService();
       //     invokeMethod(TaskService.class, request, taskService);
        }
    }

    public void doKieSessionOperation(ServerConsoleRequest request) {
        if( true ) { 
            logger.info(request.domainName + ":" + request.objectClass + "." + request.methodName);
        }
        else { 
//            KieSession kieSession = getRuntime(domainName, kieSessionid).getKieSession();
      //      invokeMethod(KieSession.class, request, kieSession);
        }
    }

    /**
    private void invokeMethod(Class<?> serviceClass, String methodName, Object serviceObject) { 
        Class<?> [] paramTypes = new Class<?> [request.numArguments];
        for( int i = 0; i <request.args.size(); ++i ) { 
           paramTypes[i]  = request.args.get(i).getClass();
        }
        try {
            Method taskMethod = serviceClass.getMethod(methodName, paramTypes);
            Object result = taskMethod.invoke(serviceObject, request.args);
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
    }
    **/
    
    private void handlException(ServerConsoleRequest request, Exception e) { 
        logger.error("Failed to invoke method " + request.objectClass + "." + request.methodName, e);
        e.printStackTrace();
    }
    
    /** 
     * add when org.kie/org.jbpm dependencies are added
     * 
    public org.kie.internal.runtime.manager.Runtime getRuntime(String domainName, String ksessionId) {
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
    **/
    
    public ServerConsoleRequest hashMaptoServerConsoleRequest(HashMap<Object, Object> hashMap) throws JMSException {
        ServerConsoleRequest request = new ServerConsoleRequest();
        
        MapMessageEnum[] keys = { MapMessageEnum.DomainName, MapMessageEnum.KieSessionId, 
                MapMessageEnum.ObjectClass, MapMessageEnum.MethodName };
        for (MapMessageEnum key : keys) {
            String value = (String) hashMap.get(key.toString());
            switch (key) {
            case DomainName:
                request.domainName = value;
                break;
            case KieSessionId:
                request.kieSessionid = value;
                break;
            case ObjectClass:
                request.objectClass = value;
                break;
            case MethodName:
                request.methodName = value;
                break;
            default:
                throw new IllegalStateException("Unexpected key in JMS message: " + key.toString());
            }
        }

        MapMessageEnum key = MapMessageEnum.NumArguments;
        int numArgs = (Integer) hashMap.get(key.toString());
        request.numArguments = numArgs;

        for( int i = 0; i < numArgs; ++i ) {
           request.args.add(hashMap.get(String.valueOf(i)));
        }
        
        return request;
    }
}
