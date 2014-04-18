package org.kie.services.remote.ws.wsdl;

/**
 * All of the possible types of Service Messages.
 *
 * @author Michel de Blok
 * @version $Date$ $Revision$ $Name$
 */
public enum ServiceMeldingType {

	/**
	 * Types are similar to logging levels as for e.g. Log4J:
	 * 
     * ERROR: The message contains an error (default value)
	 * DEBUG: The message contains (low-level) information for debugging service behavior.
     * INFO: The message contains useful information for tracing service behavior.
     * WARN: The message contains a warning about a possible fault situation.
     * FATAL: The message contains a serious (high-priority) error.
     */
	ERROR, DEBUG, INFO, WARN, FATAL

}
