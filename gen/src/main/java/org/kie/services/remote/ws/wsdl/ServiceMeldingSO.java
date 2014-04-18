package org.kie.services.remote.ws.wsdl;

public class ServiceMeldingSO extends AbstractServiceObject {
    
	/** Generated serialVersionUID. */ 
	private static final long serialVersionUID = 506888972515296934L;
	/** Fields. */
	protected String _code = null;
	protected String _omschrijving = null;
	protected ServiceMeldingType _type = ServiceMeldingType.ERROR;
	protected String _variabele = null;

    /**
     * Default constructor.
     */
    public ServiceMeldingSO() {
		super();
	}

    /**
     * Constructor.
     * 
     * @param code code
     * @param omschrijving description
     */
    public ServiceMeldingSO(final String code, final String omschrijving) {
    	this(code, omschrijving, ServiceMeldingType.ERROR);
    }

    /**
     * Constructor.
     * 
     * @param code code
     * @param omschrijving description
     * @param type type
     */
    public ServiceMeldingSO(final String code, final String omschrijving, final ServiceMeldingType type) {
    	this(code, omschrijving, type, null);
    }

    /**
     * Constructor.
     * 
     * @param code code
     * @param omschrijving description
     * @param variabele variable
     */
    public ServiceMeldingSO(final String code, final String omschrijving, final String variabele) {
    	this(code, omschrijving, ServiceMeldingType.ERROR, variabele);
    }

    /**
     * Constructor.
     * 
     * @param code code
     * @param omschrijving description
     * @param type type
     * @param variabele variable
     */
    public ServiceMeldingSO(final String code, final String omschrijving, final ServiceMeldingType type, final String variable) {
    }

    /**
     * @return code.
     */
    public String getCode() {
        return _code;
    }

    /**
     * @param code code.
     */
    public void setCode(final String code) {
        _code = code;
    }

    /**
     * @return description.
     */
    public String getOmschrijving() {
        return _omschrijving;
    }

    /**
     * @param omschrijving description
     */
    public void setOmschrijving(final String omschrijving) {
        _omschrijving = omschrijving;
    }

    /**
     * @return type.
     */
    public ServiceMeldingType getType() {
        return _type;
    }

    /**
     * @param type type
     */
    public void setType(final ServiceMeldingType type) {
        _type = type;
    }

	/**
     * @return variable name
     */
    public String getVariabele() {
        return _variabele;
    }

    /**
     * @param variabele vaiable name
     */
    public void setVariabele(String variabele) {
        _variabele = variabele;
    }

}
