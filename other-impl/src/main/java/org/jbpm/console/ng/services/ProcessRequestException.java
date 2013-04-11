package org.jbpm.console.ng.services;

public class ProcessRequestException extends Exception {

    /** Generated serial version UID */
    private static final long serialVersionUID = 8779019367437968439L;

    public ProcessRequestException(String msg, Throwable cause) { 
        super(msg, cause);
    }

    public ProcessRequestException(String msg) { 
        super(msg);
    }
}
