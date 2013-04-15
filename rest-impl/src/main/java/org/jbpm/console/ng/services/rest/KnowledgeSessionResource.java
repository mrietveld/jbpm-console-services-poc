package org.jbpm.console.ng.services.rest;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.kie.internal.runtime.manager.RuntimeEngine;

@Path("/")
@RequestScoped
public class KnowledgeSessionResource extends AbstractRuntimeResource {

    @POST
    @Path("{oper: [a-zA-Z]*}")
    public void kieSessionOperation(@Context UriInfo uriInfo, @PathParam("oper") String operation) { 
       RuntimeEngine runtimeEngine = getRuntimeEngine(null);
    }
    
    @GET
    @Path("id/{sessionId: [a-zA-Z0-9-]*}")
    public void kieSessionStatus(@PathParam("sessionId") String sessionId) { 
       RuntimeEngine runtimeEngine = getRuntimeEngine(sessionId);
        
    }
    
    @POST
    @Path("id/{sessionId: [a-zA-Z0-9-]*}/{oper: [a-zA-Z]*}")
    public void kieSessionOperation(@PathParam("sessionId") String sessionId) { 
       RuntimeEngine runtimeEngine = getRuntimeEngine(sessionId);
        
    }
    
    @GET
    @Path("procInst/{id: [0-9]*}")
    public void processInstanceStatus() { 
        // no session ID? 
    }
    
    @POST
    @Path("procInst/{id: [0-9]}/{oper: [a-zA-Z]*}")
    public void processInstanceOperation() { 
        
    }

}
