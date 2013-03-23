package org.jbpm.console.ng.services.rest.task;

import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import org.jbpm.console.ng.services.rest.AbstractRuntimeResource;
import org.kie.internal.runtime.manager.RuntimeManager;
import org.kie.internal.task.api.TaskService;

@RequestScoped
public class TaskServiceResource extends AbstractRuntimeResource {

    private TaskService taskService;
    
    public void init(RuntimeManager runtimeManager) { 
        init(runtimeManager);
        this.taskService = this.kieRuntime.getTaskService();
    }
    
    public void init(RuntimeManager runtimeManager, String ksessionId) { 
        init(runtimeManager);
        this.taskService = this.kieRuntime.getTaskService();
    }
    
    @PreDestroy
    public void dispose() { 
       this.taskService = null;
       super.dispose();
    }
    
    @POST
    @Path("{id : \\d+}/activate")
    public void activate(@PathParam("id") Long taskId, @QueryParam("userId") String userId) { 
        this.taskService.activate(taskId, userId);
    }
    
    @POST
    @Path("{id : \\d+}/claim")
    public void claim(@PathParam("id") Long taskId, @QueryParam("userId") String userId) { 
        this.taskService.claim(taskId, userId);
    }
    
    @POST
    @Path("{id : \\d+}/delegate")
    public void complete(@PathParam("id") Long taskId, @QueryParam("userId") String userId, @QueryParam("data") String data) { 
        this.taskService.delegate(taskId, userId, data);
    }
    
    @POST
    @Path("{id : \\d+}/exit")
    public void exit(@PathParam("id") Long taskId, @QueryParam("userId") String userId) { 
        this.taskService.exit(taskId, userId);
    }
    
    @POST
    @Path("{id : \\d+}/forward")
    public void forward(@PathParam("id") Long taskId, @QueryParam("userId") String userId, @QueryParam("targetId") String targetId) { 
        this.taskService.forward(taskId, userId, targetId);
    }
    
    @POST
    @Path("{id : \\d+}/release")
    public void release(@PathParam("id") Long taskId, @QueryParam("userId") String userId) { 
        this.taskService.release(taskId, userId);
    }
    
    @POST
    @Path("{id : \\d+}/remove")
    public void remove(@PathParam("id") Long taskId, @QueryParam("userId") String userId) { 
        this.taskService.remove(taskId, userId);
    }
    
    @POST
    @Path("{id : \\d+}/resume")
    public void resume(@PathParam("id") Long taskId, @QueryParam("userId") String userId) { 
        this.taskService.resume(taskId, userId);
    }
    
    @POST
    @Path("{id : \\d+}/skip")
    public void skip(@PathParam("id") Long taskId, @QueryParam("userId") String userId) { 
        this.taskService.skip(taskId, userId);
    }
    
    @POST
    @Path("{id : \\d+}/start")
    public void start(@PathParam("id") Long taskId, @QueryParam("userId") String userId) { 
        this.taskService.start(taskId, userId);
    }
    
    @POST
    @Path("{id : \\d+}/stop")
    public void stop(@PathParam("id") Long taskId, @QueryParam("userId") String userId) { 
        this.taskService.stop(taskId, userId);
    }
    
    @POST
    @Path("{id : \\d+}/suspend")
    public void suspend(@PathParam("id") Long taskId, @QueryParam("userId") String userId) { 
        this.taskService.suspend(taskId, userId);
    }
    
}
