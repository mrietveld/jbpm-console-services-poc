package org.jbpm.console.ng.services.client.api;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import javax.jms.Session;
import javax.xml.bind.JAXBException;

import org.jbpm.console.ng.services.client.message.ServiceMessage;
import org.jbpm.console.ng.services.client.message.serialization.MessageSerializationProvider;
import org.jbpm.console.ng.services.client.message.serialization.impl.jaxb.JaxbSerializationProvider;
import org.jbpm.console.ng.services.client.message.serialization.impl.jaxb.JaxbServiceMessage;

public abstract class AbstractServiceRequestProxy implements InvocationHandler {

    protected ServiceMessage request;
    protected final MessageSerializationProvider serializationProvider;

    protected static Set<String> unsupportedMethods = new HashSet<String>();
    static {
        Method[] objectMethods = Object.class.getMethods();
        for (Method objMethod : objectMethods) {
            unsupportedMethods.add(objMethod.getName());
        }
    }

    // package level constructor
    protected AbstractServiceRequestProxy(String domainName, MessageSerializationProvider serializationProvider) {
        // Message
        this.request = new ServiceMessage(domainName);
        this.serializationProvider = serializationProvider;
    }

    public abstract Object invoke(Object proxy, Method method, Object[] args) throws Throwable;

    protected Object handleMessageHolderMethodsAndUnsupportedMethods(Method method, Object[] args) {
        if (MessageHolder.class.equals(method.getDeclaringClass())) {
            ServiceMessage request = this.request;
            this.request = new ServiceMessage(this.request);
            if ("getRequest".equals(method.getName())) {
                return request;
            } else if ("createJmsMessage".equals(method.getName())) {
                try {
                    return serializationProvider.convertServiceMessageToJmsMessage(request, (Session) args[0]);
                } catch (Exception e) {
                    throw new RuntimeException("Unable to convert request to message: " + e.getMessage(), e);
                }
            } else if ("getMessageJaxbXml".equals(method.getName())) {
                JaxbServiceMessage jaxbRequest = new JaxbServiceMessage(request);
                try {
                    return JaxbSerializationProvider.convertJaxbObjectToString(jaxbRequest);
                } catch (JAXBException jaxbe) {
                    throw new RuntimeException("Unable to convert request to XML string: " + jaxbe.getMessage(), jaxbe);
                }
            }
        }

        // No object methods (.wait(), .clone(), etc.. ) supported
        if (unsupportedMethods.contains(method.getName())) {
            throw new UnsupportedOperationException(method.getName() + " is unsupported as a request method!");
        }

        return null;
    }

}
