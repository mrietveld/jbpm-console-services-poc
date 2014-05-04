package org.kie.services.remote.ws.sei.command;

import org.kie.services.remote.ws.common.KieRemoteWebServiceException;
import org.kie.services.remote.ws.common.WebServiceFaultInfo;

public class CommandWebServiceException extends KieRemoteWebServiceException {

    /** default serial version UID */
    private static final long serialVersionUID = 2301L;

    public CommandWebServiceException(String message, WebServiceFaultInfo faultInfo) {
        super(message, faultInfo);
    }

}
