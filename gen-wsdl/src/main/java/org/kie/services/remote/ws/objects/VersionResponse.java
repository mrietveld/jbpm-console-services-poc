package org.kie.services.remote.ws.objects;

/**
 * This is used to return the version of a service.
 */
public class VersionResponse extends SerializableServiceObject {

	/** Serial Version UID. */
    private static final long serialVersionUID = -374376680962126054L;
    
    /** The version. */
	private String _version;
	
	/**
	 * Constructor.
	 */
	public VersionResponse() {
		this("unknown");
	}
	
	/**
	 * Constructor.
	 * 
	 * @param version The version.
	 */
	public VersionResponse(String version) {
		_version = version;
	}

	/**
	 * @return The version.
	 */
	public String getVersion() {
		return _version;
	}

	/**
	 * @param version The version to set.
	 */
	public void setVersion(String version) {
		_version = version;
	}
	
}
