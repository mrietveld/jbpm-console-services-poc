package org.kie.services.remote.ws.wsdl;

/**
 * All of the possible types of Service exceptions.
 *
 * @author Michel de Blok
 * @version $Date$ $Revision$ $Name$
 */
public enum ExceptionType {

    // Generic type for internal system faults (default value)
	SYSTEM, 
	// Problem with input parameters: correct input
	VALIDATION, 
	// Problem with authorisation: try again with other credentials
	PERMISSION, 
	// Problem with connection to a backend component, try again later.
	CONNECTION

}
