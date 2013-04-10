package org.jbpm.console.ng.services.client.jms;

import java.util.HashMap;

import org.jbpm.console.ng.services.client.api.fluent.FluentApiRequestFactoryImpl;
import org.jbpm.console.ng.services.client.api.remote.RemoteApiRequestFactoryImpl;
import org.jbpm.console.ng.services.client.api.same.jms.SameApiRequestFactoryImpl;

public class ServiceRequestFactoryProvider {
    
    private static HashMap<RequestApiType, Object> instanceMap = new HashMap<ServiceRequestFactoryProvider.RequestApiType, Object>();
    
    public static SameApiRequestFactoryImpl createNewSameApiInstance() { 
        loadClass(SameApiRequestFactoryImpl.class);
        return (SameApiRequestFactoryImpl) instanceMap.get(RequestApiType.ORIGINAL);
    }
    
    public static RemoteApiRequestFactoryImpl createNewRemoteApiInstance() { 
        loadClass(RemoteApiRequestFactoryImpl.class);
        return (RemoteApiRequestFactoryImpl) instanceMap.get(RequestApiType.REMOTE);
    }
    
    public static FluentApiRequestFactoryImpl createNewFluentApiInstance() { 
        loadClass(FluentApiRequestFactoryImpl.class);
        return (FluentApiRequestFactoryImpl) instanceMap.get(RequestApiType.FLUENT);
    }
    
    private static void loadClass(Class clazz) {
        try {
            Class.forName(clazz.getName(), true, clazz.getClassLoader());
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }
    
    public static void setInstance(RequestApiType apiType, Object object) { 
        instanceMap.put(apiType, object);
    }
    

    public enum RequestApiType {
        ORIGINAL, REMOTE, FLUENT;
    }
}
