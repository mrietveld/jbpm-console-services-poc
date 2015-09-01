package org.custom.process.listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.jbpm.runtime.manager.api.qualifiers.Process;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.internal.runtime.manager.EventListenerProducer;

/**
 * See http://docs.jboss.org/jbpm/v6.2/userguide/jBPMCoreEngine.html#d0e2270
 */
@Process
public class CustomEventListenerProducer implements EventListenerProducer<CustomProcessEventListener> {

    @Inject
    private RuntimeManager runtimeManager;

    @Override
    public List<CustomProcessEventListener> getEventListeners( String identifier, Map<String, Object> params ) {
        CustomProcessEventListener[] listenerArr = { new CustomProcessEventListener(runtimeManager) };
        // create a new ArrayList because Arrays.asList() returns an unmodifiable list
        // (and I'm not sure if the returned list will be modified somewhere?)
        return new ArrayList<CustomProcessEventListener>(Arrays.asList(listenerArr));
    }

}
