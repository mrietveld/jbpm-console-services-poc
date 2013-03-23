package org.jbpm.console.ng.services.rest.ksession;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import org.jbpm.console.ng.services.client.ProcessInfo;
import org.jbpm.console.ng.services.rest.AbstractRuntimeResource;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.internal.KieInternalServices;
import org.kie.internal.process.CorrelationKeyFactory;
import org.kie.internal.runtime.manager.RuntimeManager;

@RequestScoped
public class KnowledgeSessionResource extends AbstractRuntimeResource {

    private KieSession kieSession; 
    
    public void init(RuntimeManager runtimeManager) {
        init(runtimeManager);
        this.kieSession = this.kieRuntime.getKieSession();
    }

    public void init(RuntimeManager runtimeManager, String ksessionId) {
        init(runtimeManager, ksessionId);
        this.kieSession = this.kieRuntime.getKieSession();
    }
    
    @PreDestroy
    public void dispose() { 
        this.kieSession.dispose();
        this.kieSession = null;
        super.dispose();
    }
    
    @POST
    @Path("/startProcess")
    public ProcessInfo startProcess(@QueryParam("processId") String processId, @QueryParam("params") Map<String, String> parameters) {
        processId = processId.toLowerCase();

        Map<String, Object> convertedParams = new HashMap<String, Object>();
        if (parameters != null && parameters.size() > 0) {
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                if (entry.getKey() != null) {
                    convertedParams.put(entry.getKey().toLowerCase(), entry.getValue().toLowerCase());
                }
            }
        } else {
            parameters = null;
        }

        ProcessInstance processInstance;
        if( parameters == null ) { 
            processInstance = this.kieSession.startProcess(processId);
        } else { 
            processInstance = this.kieSession.startProcess(processId, convertedParams);
        }
        
        return new ProcessInfo(processInstance);
    }

    @POST
    @Path("/signalEvent")
    public void signalEvent(@QueryParam("type") String type, @QueryParam("event") String event) { 
        kieSession.signalEvent(type, event);
    }

    @POST
    @Path("/fireAllRules")
    public void fireAllRules() { 
        try {
            kieSession.fireAllRules();
        } catch (Throwable t) {
            // exception mapper?
        }
    }

    @GET
    @Path("/procInst/{id: \\d+}")
    public ProcessInfo getProcessInstanceInfo(@PathParam("id") String processInstanceId) {
        try {
            long longId = Long.parseLong(processInstanceId);
            ProcessInstance processInstance = kieSession.getProcessInstance(longId, true);
            return new ProcessInfo(processInstance);
        } catch (NumberFormatException nfe) {
            // exception mapper?
        }

        return null;
    }
    
    @POST
    @Path("/procInst/{id: \\d+}/abort")
    public void abortProcessInstance(@PathParam("id") Long processInstanceId) {
        try {
            kieSession.abortProcessInstance(processInstanceId);
        } catch (NumberFormatException nfe) {
            // exception mapper?
        }
    }

    @POST
    @Path("/procInst/{id: \\d+}/signalEvent")
    public void signalProcessInstanceEvent(@PathParam("id") Long processInstanceId, 
            @QueryParam("type") String type, @QueryParam("event") String event) {
        try {
            kieSession.signalEvent(type, event, processInstanceId);
        } catch (Throwable t) {
            // exception mapper?
        }
    }

}
