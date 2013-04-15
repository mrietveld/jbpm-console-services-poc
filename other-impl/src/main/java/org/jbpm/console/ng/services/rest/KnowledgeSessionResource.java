package org.jbpm.console.ng.services.rest;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.jbpm.console.ng.services.client.jms.ServiceMessage;
import org.jbpm.console.ng.services.client.jms.ServiceMessage.OperationMessage;
import org.jbpm.console.ng.services.client.jms.ServiceMessageValidator;
import org.kie.api.runtime.KieSession;

@Path("/")
@RequestScoped
public class KnowledgeSessionResource extends AbstractRuntimeResource {

    @POST
    @Path("{oper: [a-zA-Z]+}")
    public void kieSessionOperation(@Context UriInfo uriInfo, @PathParam("oper") String operName) {
        OperationMessage response = handleOperation(null, operName, null);
    }

    @GET
    @Path("id/{sessionId: [0-9]+}")
    public void kieSessionStatus(@PathParam("sessionId") String sessionId) {
        ServiceMessage message = new ServiceMessage();
        message.setDomainName(this.domainName);

        // TODO: finish
        OperationMessage operMsg = new OperationMessage("getProcessInstanceStauts..", ServiceMessage.KIE_SESSION_REQUEST);

        this.processRequestBean.doKieSessionOperation(message, operMsg);
    }

    @POST
    @Path("id/{sessionId: [0-9]+}/{oper: [a-zA-Z]+}")
    public void kieSessionOperation(@PathParam("sessionId") String sessionId, @PathParam("oper") String operName) {
        OperationMessage response = handleOperation(sessionId, operName, null);
        // TODO: handle response
    }
    
    @GET
    @Path("procInst/{id: [0-9]+}")
    public void processInstanceStatus() {
        // no session ID?
    }

    @POST
    @Path("procInst/{procInstId: [0-9]+}/{oper: [a-zA-Z]*}")
    public void processInstanceOperation(@PathParam("procInstId") Long processInstanceId, @PathParam("oper") String operName) {
        OperationMessage operMsg = handleOperation(null, operName, processInstanceId);
        // TODO: handle response
    }

    private OperationMessage handleOperation(String sessionId, String operName, Long procesInstanceId) { 
        ServiceMessage message = new ServiceMessage(this.domainName, sessionId);
        OperationMessage operMsg = new OperationMessage(operName, ServiceMessage.KIE_SESSION_REQUEST);
        if( ! ServiceMessageValidator.operationIsValid(operMsg, KieSession.class) ) { 
            // return "failed operation" thing.. 
        }
        return this.processRequestBean.doKieSessionOperation(message, operMsg);
    }

}
