package org.jbpm.console.ng.services.rest;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import org.jbpm.console.ng.services.UnfinishedError;
import org.jbpm.console.ng.services.client.message.ServiceMessage;
import org.jbpm.console.ng.services.client.message.ServiceMessage.OperationMessage;
import org.jbpm.console.ng.services.client.message.serialization.impl.jaxb.JaxbSerializationProvider;
import org.jbpm.console.ng.services.client.message.serialization.impl.jaxb.JaxbServiceMessage;
import org.jbpm.console.ng.services.ejb.ProcessRequestBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO:
 * current uRL structure
 * 
 * |{domain}|session|{oper} or |{domain}|task|{id}|oper
 * 
 * but a better idea is probablyt the following, given that ServiceMessage already includes the domain info
 * 
 * |domain|{domain}|session|{oper} etc.
 * |all|session|{oper}
 * 
 * TODO:
 * 
 * Add execption mappers for thrown exceptions
 */

@Path("/{domain : [a-zA-z0-9-]+}")
@RequestScoped
public class DomainResource {

    private Logger logger = LoggerFactory.getLogger(DomainResource.class);

    @PathParam("domain")
    private String domainName;

    @EJB
    protected ProcessRequestBean processRequestBean;

    private JaxbSerializationProvider jaxbSerializationProvider = new JaxbSerializationProvider();

    // Helper methods ------------------------------------------------------------------------------------------------------------

    private ServiceMessage convertQueryParamsToServiceMsg(MultivaluedMap<String, String> queryParams, int serviceType, String operName, Long objectId) {
        throw new UnfinishedError("Conversion from query params to Service Message not done yet.");
    }
    
    private JaxbServiceMessage handleOperationRequestWithParams(UriInfo uriInfo, int serviceType, String operName, Long objectId) { 
        // TODO: devise something to handle maps as query params
        ServiceMessage serviceRequest = convertQueryParamsToServiceMsg(uriInfo.getQueryParameters(), serviceType, operName, objectId);
        // TODO: limit client api when sending REST request -- or otherwise expand this to do batch operations
        OperationMessage responseOper = this.processRequestBean.doOperation(serviceRequest, serviceRequest.getOperations().get(0));

        ServiceMessage responseMsg = new ServiceMessage(serviceRequest.getDomainName());
        responseMsg.addOperation(responseOper);
        return new JaxbServiceMessage(responseMsg);
    }
    
    private JaxbServiceMessage handleOperationRequestWithXml(JaxbServiceMessage xmlRequest, String operName, Long objectId) 
            throws Exception { 
        ServiceMessage serviceRequest = jaxbSerializationProvider.convertJaxbRequesMessageToServiceMessage(xmlRequest);
        // TODO: objectId: trust the URL or the xml?  (==> batch xml or not? )
        // TODO: limit client api when sending REST request -- or otherwise expand this to do batch operations
        OperationMessage responseOper = this.processRequestBean.doOperation(serviceRequest, serviceRequest.getOperations().get(0));

        ServiceMessage responseMsg = new ServiceMessage(serviceRequest.getDomainName());
        responseMsg.addOperation(responseOper);
        return new JaxbServiceMessage(responseMsg);
    }


    // KieSession methods -------------------------------------------------------------------------------------------------------

    @POST
    @Path("session/{oper: [a-zA-Z]+}")
    @Produces(MediaType.APPLICATION_XML)
    public JaxbServiceMessage kieSessionOperationWithParams(@Context UriInfo uriInfo, @PathParam("oper") String operName) {
        return handleOperationRequestWithParams(uriInfo, ServiceMessage.KIE_SESSION_REQUEST, operName, null);
    }

    @POST
    @Path("session/{oper: [a-zA-Z]+}")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public JaxbServiceMessage kieSessionOperationWithXml(@PathParam("oper") String operName, JaxbServiceMessage xmlRequest)
            throws Exception {
        return handleOperationRequestWithXml(xmlRequest, operName, null);
    }

    @GET
    @Path("{domain : [a-zA-z0-9-]+}/session/procInst/{id: [0-9]+}")
    @Produces(MediaType.APPLICATION_XML)
    public void processInstanceStatus(@PathParam("id") Long procesInstanceId) {
        // TODO
        if (false) {
            String operName = null;
            // ...
        }
        throw new UnfinishedError("Process instance status retrievel has not yet been implemented.");
    }

    @POST
    @Path("session/procInst/{procInstId: [0-9]+}/{oper: [a-zA-Z]*}")
    @Produces(MediaType.APPLICATION_XML)
    public JaxbServiceMessage processInstanceOperationWithParams(@Context UriInfo uriInfo,
            @PathParam("procInstId") Long processInstanceId, @PathParam("oper") String operName) {
        return handleOperationRequestWithParams(uriInfo, ServiceMessage.KIE_SESSION_REQUEST, operName, null);
    }

    @POST
    @Path("session/procInst/{procInstId: [0-9]+}/{oper: [a-zA-Z]*}")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public JaxbServiceMessage processInstanceOperationWithXml(@PathParam("procInstId") Long processInstanceId,
            @PathParam("oper") String operName, JaxbServiceMessage xmlRequest) throws Exception {
        return handleOperationRequestWithXml(xmlRequest, operName, null);
    }

    // TaskService methods ------------------------------------------------------------------------------------------------------

    @POST
    @Path("task/{id : \\d+}/{oper : [a-zA-Z]+}")
    @Consumes(MediaType.APPLICATION_XML)
    public JaxbServiceMessage taskOperationWithParams(@Context UriInfo uriInfo, @PathParam("id") Long taskId,
            @PathParam("oper") String operName) {
        return handleOperationRequestWithParams(uriInfo, ServiceMessage.TASK_SERVICE_REQUEST, operName, null);
    }

    @POST
    @Path("task/{id : \\d+}/{oper : [a-zA-Z]+}")
    @Consumes(MediaType.APPLICATION_XML)
    public JaxbServiceMessage taskOperationWithXml(@PathParam("id") Long taskId, @PathParam("oper") String operName, 
            JaxbServiceMessage xmlRequest) throws Exception {
        return handleOperationRequestWithXml(xmlRequest, operName, null);
    }

}
