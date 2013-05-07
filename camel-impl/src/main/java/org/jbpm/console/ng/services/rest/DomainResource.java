package org.jbpm.console.ng.services.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.jbpm.console.ng.services.client.message.serialization.impl.jaxb.JaxbServiceMessage;

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

@Path("/")
public interface DomainResource {

    @POST
    @Path("{domain : [a-zA-z0-9-]+}/session/{oper: [a-zA-Z]+}")
    @Produces(MediaType.APPLICATION_XML)
    JaxbServiceMessage kieSessionOperationWithParams(@Context UriInfo uriInfo, @PathParam("domain") String domainName, @PathParam("oper") String operName);

    @POST
    @Path("{domain : [a-zA-z0-9-]+}/session/{oper: [a-zA-Z]+}")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    JaxbServiceMessage kieSessionOperationWithXml(@PathParam("domain") String domainName, @PathParam("oper") String operName, JaxbServiceMessage xmlRequest);

    @GET
    @Path("{domain : [a-zA-z0-9-]+}/session/procInst/{id: [0-9]+}")
    @Produces(MediaType.APPLICATION_XML)
    public void processInstanceStatus(@PathParam("domain") String domainName, @PathParam("id") Long procesInstanceId);

    @POST
    @Path("{domain : [a-zA-z0-9-]+}/session/procInst/{procInstId: [0-9]+}/{oper: [a-zA-Z]*}")
    @Produces(MediaType.APPLICATION_XML)
    public JaxbServiceMessage processInstanceOperationWithParams(@Context UriInfo uriInfo, @PathParam("domain") String domainName, 
            @PathParam("procInstId") Long processInstanceId, @PathParam("oper") String operName); 

    @POST
    @Path("{domain : [a-zA-z0-9-]+}/session/procInst/{procInstId: [0-9]+}/{oper: [a-zA-Z]*}")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public JaxbServiceMessage processInstanceOperationWithXml(@PathParam("domain") String domainName, @PathParam("procInstId") Long processInstanceId,
            @PathParam("oper") String operName, JaxbServiceMessage xmlRequest) throws Exception; 

    // TaskService methods ------------------------------------------------------------------------------------------------------

    @POST
    @Path("{domain : [a-zA-z0-9-]+}/task/{id : \\d+}/{oper : [a-zA-Z]+}")
    @Consumes(MediaType.APPLICATION_XML)
    public JaxbServiceMessage taskOperationWithParams(@Context UriInfo uriInfo, @PathParam("domain") String domainName, @PathParam("id") Long taskId,
            @PathParam("oper") String operName); 

    @POST
    @Path("{domain : [a-zA-z0-9-]+}/task/{id : \\d+}/{oper : [a-zA-Z]+}")
    @Consumes(MediaType.APPLICATION_XML)
    public JaxbServiceMessage taskOperationWithXml(@PathParam("domain") String domainName, @PathParam("id") Long taskId, @PathParam("oper") String operName, 
            JaxbServiceMessage xmlRequest) throws Exception;

}
