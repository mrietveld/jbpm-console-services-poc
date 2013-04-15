package org.jbpm.console.ng.services.rest;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.jbpm.runtime.manager.api.RuntimeManagerCacheEntryPoint;

@Path("/")
@RequestScoped
public class TaskServiceResource extends AbstractRuntimeResource {

    private String domainName;
    
    @Inject
    RuntimeManagerCacheEntryPoint runtimeManagerCache;
    
    public void setDomainName(String domain) { 
        this.domainName = domain;
    }
    
    @POST
    @Path("{id : \\d+}/{oper : [a-zA-Z]+")
    public void activate(@Context UriInfo uriIno, @PathParam("id") Long taskId, @PathParam("oper") String oper) { 
        
    }
    
}
