package org.jbpm.console.ng.services.rest;

import javax.ejb.EJB;

import org.jbpm.console.ng.services.ejb.ProcessRequestBean;


public abstract class AbstractRuntimeResource {

    protected String domainName;

    @EJB
    protected ProcessRequestBean processRequestBean;
    
    public void setDomainName(String domainName) { 
        this.domainName = domainName;
    }
    
}
