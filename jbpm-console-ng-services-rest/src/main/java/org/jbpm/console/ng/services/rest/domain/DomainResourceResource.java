package org.jbpm.console.ng.services.rest.domain;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.jbpm.console.ng.services.shared.DomainRuntimeManagerProvider;
import org.kie.internal.runtime.manager.RuntimeManager;

@Path("/")
@RequestScoped
public class DomainResourceResource {

    @Inject
    private DomainRuntimeManagerProvider domainRuntimeManagerProvider;
    
    @Inject
    private DomainResource domainResource;
    
    @Path("{id : [a-zA-z0-9-]+}")
    public Object getDomainResource(@PathParam("id") String domainName) {
        RuntimeManager rm = domainRuntimeManagerProvider.getRuntimeManager(domainName);
        domainResource.setRuntimeManager(rm);
        return domainResource;
    }

}
