package org.jbpm.console.ng.services.rest;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;


@Path("/")
@RequestScoped
public class DomainResource {

    @Inject
    private KnowledgeSessionResource knowledgeSessionResource;
    @Inject
    private TaskServiceResource taskServiceResource;

    @Path("{domain : [a-zA-z0-9-]+}/session")
    public KnowledgeSessionResource getNewKnowledgeSessionResource(@PathParam("domain") String domainName) { 
        knowledgeSessionResource.setDomainName(domainName);
        return knowledgeSessionResource;
    }
    
    @Path("{domain : [a-zA-z0-9-]+}/task")
    public TaskServiceResource getTaskServiceResource(@PathParam("domain") String domainName) { 
        taskServiceResource.setDomainName(domainName);
        return taskServiceResource;
    }
    
}
