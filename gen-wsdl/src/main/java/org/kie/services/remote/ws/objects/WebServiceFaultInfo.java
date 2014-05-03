package org.kie.services.remote.ws.objects;

/**
 * This the information for a web service fault.
 * 
 */
public class WebServiceFaultInfo extends SerializableServiceObject {

	/** Generated Serial version UID. */
    private static final long serialVersionUID = -8214848295651544674L;
    
    /** Type of the exception. */
    protected ExceptionType type;
    
    /** For matching the response to the request. */
    protected String correlationId;

    /**
     * @return type type of exception
     */
    public ExceptionType getType() {
        return type;
    }

    /**
     * @param type type of exception
     */
    public void setType(ExceptionType type) {
        this.type = type;
    }

    /**
     * @return Correlation Id
     */
    public String getCorrelationId() {
        return correlationId;
    }

    /**
     * @param correlationId Correlation Id
     */
    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

}
