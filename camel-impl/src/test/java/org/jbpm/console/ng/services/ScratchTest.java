package org.jbpm.console.ng.services;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.jbpm.console.ng.services.client.api.MessageHolder;
import org.jbpm.console.ng.services.rest.DomainResource;
import org.junit.Test;

public class ScratchTest {

    @Test
    public void scratch() { 
        Method [] classMethods = MessageHolder.class.getMethods();
        String [] classMethodNames = new String[classMethods.length];
        
        for( int i = 0; i < classMethods.length; ++i ) { 
            classMethodNames[i] = classMethods[i].getName();
        }
        
        Arrays.sort(classMethodNames);
        for( String og : classMethodNames ) { 
            System.out.println( og );
        }
    }
}
