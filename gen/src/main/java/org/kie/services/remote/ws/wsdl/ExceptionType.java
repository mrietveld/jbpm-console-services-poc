package org.kie.services.remote.ws.wsdl;

public enum ExceptionType {

	/**
     * System, generic type for internal system faults (default value)
     * Validation, problem with input params, correct input.
     * Autorisation, problem with autorisation, try again with other credentials.
     * Connection, problem with connection to a backend component, try again later.
     */
	SYSTEM, VALIDATION, AUTHORISATION, CONNECTION

}
