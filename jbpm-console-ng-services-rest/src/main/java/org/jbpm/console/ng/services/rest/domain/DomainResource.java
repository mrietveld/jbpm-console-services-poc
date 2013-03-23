package org.jbpm.console.ng.services.rest.domain;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.jbpm.console.ng.services.rest.ksession.KnowledgeSessionResource;
import org.jbpm.console.ng.services.rest.task.TaskServiceResource;
import org.kie.internal.runtime.manager.RuntimeManager;

@RequestScoped
public class DomainResource {

    private RuntimeManager runtimeManager;
    
    @Inject
    private KnowledgeSessionResource knowledgeSessionResource;
    @Inject
    private TaskServiceResource taskServiceResource;
    
    public void setRuntimeManager(RuntimeManager runtimeManager) { 
        this.runtimeManager = runtimeManager;
    }
    
    @Path("session/id/{id: [a-zA-Z0-9-]*}")
    public KnowledgeSessionResource getKnowledgeSessionResource(@PathParam("id") String ksessionId) { 
        knowledgeSessionResource.init(runtimeManager, ksessionId);
        return knowledgeSessionResource;
    }
    
    @Path("session")
    public KnowledgeSessionResource getNewKnowledgeSessionResource() { 
        knowledgeSessionResource.init(runtimeManager);
        return knowledgeSessionResource;
    }
    
    
    @Path("task")
    public TaskServiceResource getTaskServiceResource() { 
        taskServiceResource.init(runtimeManager);
        return taskServiceResource;
    }
    
}
