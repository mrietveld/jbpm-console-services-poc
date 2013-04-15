package org.jbpm.console.ng.services.client.jms;

import java.lang.reflect.Method;

import org.jbpm.console.ng.services.client.jms.ServiceMessage.OperationMessage;

public class ServiceMessageValidator {

    private final Class<?>[] serviceInterfaces;

    public ServiceMessageValidator(Class<?>... serviceInteface) {
        this.serviceInterfaces = serviceInteface;
    }

    public boolean messageIsValid(ServiceMessage message) {
        return messageIsValid(message, this.serviceInterfaces);
    }

    public boolean operationIsValid(OperationMessage operationMessage) {
        return operationIsValid(operationMessage, this.serviceInterfaces);
    }

    @SuppressWarnings("rawtypes")
    public static boolean messageIsValid(ServiceMessage message, Class... serviceInterfaces) {
        for (OperationMessage operation : message.getOperations()) {
            if (!operationIsValid(operation, serviceInterfaces)) {
                return false;
            }
        }

        return true;
    }

    @SuppressWarnings("rawtypes")
    public static boolean operationIsValid(OperationMessage operMsg, Class... serviceInterfaces) {
        // Retrieve info about method (and method arguments)
        Object[] args = operMsg.getArgs();
        Class<?>[] paramTypes = new Class[args.length];
        try {
            for (int i = 0; i < args.length; ++i) {
                paramTypes[i] = args[i].getClass();
            }
        } catch (Throwable t) {
            // Unable to retrieve class from parameter? Then there's definitely something wrong
            return false;
        }

        // Check each interface
        for (Class<?> service : serviceInterfaces) {
            boolean operationExists = false;
            // Operation message may contain name with incorrect lower/upper case
            Method[] serviceMethods = service.getDeclaredMethods();
            String operName = operMsg.getMethodName();
            for (int i = 0; i < serviceMethods.length; ++i) {
                if (serviceMethods[i].getName().matches("(?i)" + operName)) {
                    // Set to correct name with lower/uppercase
                    operMsg.setMethodName(serviceMethods[i].getName());
                    operationExists = true;
                }
            }

            // Check if method name with supplied parameters exists
            if (operationExists) {
                try {
                    // See if method (with supplied parameters) actually exists
                    service.getMethod(operMsg.getMethodName(), paramTypes);
                    return true;
                } catch (Throwable t) {
                    // do nothing
                }
            }
        }

        return false;
    }
}
