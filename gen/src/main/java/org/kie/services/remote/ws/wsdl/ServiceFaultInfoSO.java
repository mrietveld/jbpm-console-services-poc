package org.kie.services.remote.ws.wsdl;

public class ServiceFaultInfoSO extends AbstractServiceObject {

	/** Generated Serial version UID. */
	private static final long serialVersionUID = -6686680449822871527L;
	
	/** Type of the exception. */
    protected ExceptionType _type;
    /** A List of ServiceMelding objects containing information. */
    protected ServiceMeldingSO[] _meldingLijst;
    /** For matching the response to the request. */
    protected String _correlationId;

	/**
	 * @return the meldingLijst
	 */
	public ServiceMeldingSO[] getMeldingLijst() {
		return _meldingLijst;
	}

	/**
	 * @param meldingLijst the meldingLijst to set
	 */
	public void setMeldingLijst(ServiceMeldingSO[] meldingLijst) {
		_meldingLijst = meldingLijst;
	}

    /**
     * @return type type of exception
     */
    public ExceptionType getType() {
        return _type;
    }

    /**
     * @param type type of exception
     */
    public void setType(ExceptionType type) {
        _type = type;
    }

    /**
     * @return Correlation Id
     */
    public String getCorrelationId() {
        return _correlationId;
    }

    /**
     * @param correlationId Correlation Id
     */
    public void setCorrelationId(String correlationId) {
        _correlationId = correlationId;
    }

}
