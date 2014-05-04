
package org.kie.services.remote.ws.common;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "KieRemoteWebServiceException", propOrder = {
    "faultInfo"
})
public class KieRemoteWebServiceException extends Exception {

    /** Default serial version UID */
    private static final long serialVersionUID = 2301L;
    
    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private WebServiceFaultInfo faultInfo;

    /**
     * 
     * @param message
     * @param faultInfo
     */
    public KieRemoteWebServiceException(String message, WebServiceFaultInfo faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param message
     * @param faultInfo
     * @param cause
     */
    public KieRemoteWebServiceException(String message, WebServiceFaultInfo faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: org.kie.services.remote.ws.wsdl.ServiceFaultInfoSO
     */
    public WebServiceFaultInfo getFaultInfo() {
        return faultInfo;
    }

}
