package org.jbpm.console.ng.services.client.api.fluent.api;

import java.util.Collection;
import java.util.Map;

import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;

public interface FluentWorkItemManagerRequest {

    /**
     * Notifies the work item manager that the work item with the given
     * id has been completed.  Results related to the execution of this
     * work item can be passed.
     *
     * @param id the id of the work item that has been completed
     * @param results the results related to this work item, or <code>null</code> if there are no results
     */
    FluentWorkItemManagerRequest completeWorkItem(long id,
                          Map<String, Object> results);

    /**
     * Notifies the work item manager that the work item with the given
     * id could not be executed and should be aborted.
     *
     * @param id the id of the work item that should be aborted
     */
    FluentWorkItemManagerRequest abortWorkItem(long id);

    /**
     * Register the given handler for all work items of the given
     * type of work
     * 
     * @param workItemName the type of work this work item handler can execute
     * @param handler the handler for executing work items
     */
    FluentWorkItemManagerRequest registerWorkItemHandler(String workItemName,
                                 WorkItemHandler handler); 
    
    FluentKieSessionRequest getFluentKieSessionRequest();
    FluentTaskServiceRequest getFluentTaskServiceRequest();

}
