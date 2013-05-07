package org.jbpm.console.ng.services.ejb;

import javax.ejb.Stateless;

import org.jbpm.console.ng.services.client.message.ServiceMessage;

@Stateless(name="Test")
public class TestEjbBean {

    public String print(Object object) { 
        System.out.println(">>> " + object.getClass().getName() );
        return object.getClass().getSimpleName();
    }
}
