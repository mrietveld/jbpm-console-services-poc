package org.jbpm.console.ng.services.rest;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.jbpm.console.ng.services.client.message.ServiceMessage;
import org.jbpm.console.ng.services.client.message.ServiceMessageValidator;
import org.jbpm.console.ng.services.client.message.ServiceMessage.OperationMessage;
import org.kie.api.runtime.KieSession;

@Path("/")
@RequestScoped
public class TaskServiceResource extends AbstractRuntimeResource {
    
    public void setDomainName(String domain) { 
        this.domainName = domain;
    }
    
    @POST
    @Path("{id : \\d+}/{oper : [a-zA-Z]+")
    public void doTaskOperation(@Context UriInfo uriIno, @PathParam("id") Long taskId, @PathParam("oper") String operName) { 
        ServiceMessage message = new ServiceMessage(this.domainName, getKieSessionId(taskId));
        OperationMessage operMsg = new OperationMessage(operName, ServiceMessage.KIE_SESSION_REQUEST);
        if( ! ServiceMessageValidator.operationIsValid(operMsg, KieSession.class) ) { 
            // return "failed operation" thing.. 
        }
        OperationMessage response = this.processRequestBean.doTaskServiceOperation(message, operMsg);
    }
    
    String getKieSessionId(Long taskId) { 
       return null; 
    }
    
}
