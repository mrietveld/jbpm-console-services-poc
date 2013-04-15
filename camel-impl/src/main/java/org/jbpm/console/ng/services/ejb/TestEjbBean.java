package org.jbpm.console.ng.services.ejb;

import javax.ejb.Stateless;

import org.jbpm.console.ng.services.client.message.ServiceMessage;

@Stateless(mappedName="TestBean")
public class TestEjbBean {

    public void print(ServiceMessage serviceMsg) { 
        System.out.println(">>> " + serviceMsg.getDomainName());
    }
}
