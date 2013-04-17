package org.jbpm.console.ng.services.rest;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.jbpm.console.ng.services.UnfinishedError;
import org.jbpm.console.ng.services.client.message.ServiceMessage;
import org.jbpm.console.ng.services.client.message.ServiceMessage.OperationMessage;
import org.jbpm.console.ng.services.client.message.ServiceMessageValidator;
import org.jbpm.console.ng.services.ejb.ProcessRequestBean;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Path("/")
@RequestScoped
public class DomainResource {

    private Logger logger = LoggerFactory.getLogger(DomainResource.class);
    
    @Context 
    UriInfo uriInfo;

    @EJB
    protected ProcessRequestBean processRequestBean;
    
    // Helper methods ------------------------------------------------------------------------------------------------------------
    
    private OperationMessage handleOperation(String domainName, int serviceType, String operName, Long procesInstanceId) { 
        ServiceMessage message = new ServiceMessage(domainName);
        OperationMessage operMsg = new OperationMessage(operName, serviceType);
        if( ! ServiceMessageValidator.operationIsValid(operMsg, KieSession.class) ) { 
            throw new UnfinishedError("What do do when the message is invalid.");
        }
        return this.processRequestBean.doOperation(message, operMsg);
    }
    
    // KieSession methods -------------------------------------------------------------------------------------------------------
    
    @POST
    @Path("{domain : [a-zA-z0-9-]+}/session/{oper: [a-zA-Z]+}")
    @Consumes(MediaType.APPLICATION_XML)
    public void kieSessionOperation(@PathParam("domain") String domainName, @PathParam("oper") String operName) {
        System.out.println( "KieSession: " + operName);
        OperationMessage response = handleOperation(domainName, ServiceMessage.KIE_SESSION_REQUEST, operName, null);
    }

    @GET
    @Path("{domain : [a-zA-z0-9-]+}/session/procInst/{id: [0-9]+}")
    public void processInstanceStatus(@PathParam("domain") String domainName) { 
        // TODO
        if( false ) { 
            String operName = null;
            OperationMessage response = handleOperation(domainName, ServiceMessage.KIE_SESSION_REQUEST, operName, null);
        } 
        throw new UnfinishedError("Process instance status retrievel has not yet been implemented.");
    }

    @POST
    @Path("{domain : [a-zA-z0-9-]+}/session/procInst/{procInstId: [0-9]+}/{oper: [a-zA-Z]*}")
    @Consumes(MediaType.APPLICATION_XML)
    public void processInstanceOperation(@PathParam("domain") String domainName, @PathParam("procInstId") Long processInstanceId, 
            @PathParam("oper") String operName) {
        OperationMessage response = handleOperation(domainName, ServiceMessage.KIE_SESSION_REQUEST, operName, processInstanceId);
    }


    // TaskService methods ------------------------------------------------------------------------------------------------------
    
    /**
     * 
     * @param uriIno
     * @param taskId
     * @param operName
     */
    @POST
    @Path("{domain: [a-zA-Z0-9-]+}/task/{id : \\d+}/{oper : [a-zA-Z]+}")
    @Consumes(MediaType.APPLICATION_XML)
    public void doTaskOperation(@PathParam("domain") String domainName, @PathParam("id") Long taskId, @PathParam("oper") String operName) { 
        OperationMessage response = handleOperation(domainName, ServiceMessage.TASK_SERVICE_REQUEST, operName, taskId);
    }
    
}
