package org.jbpm.console.ng.services.shared;

import javax.ejb.Lock;
import javax.ejb.LockType;

import org.kie.internal.runtime.manager.RuntimeManager;

public interface DomainRuntimeManagerProvider {

    @Lock(LockType.READ)
    public abstract RuntimeManager getRuntimeManager(String domainId);

}