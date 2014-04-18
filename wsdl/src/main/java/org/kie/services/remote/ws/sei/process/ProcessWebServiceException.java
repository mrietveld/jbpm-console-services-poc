
package org.kie.services.remote.ws.sei.process;

import javax.xml.ws.WebFault;
import org.kie.services.remote.ws.wsdl.ServiceFaultInfoSO;


@WebFault(name = "ProcessServiceException", targetNamespace = "http://services.remote.kie.org/process")
public class ProcessWebServiceException extends Exception {

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private ServiceFaultInfoSO faultInfo;

    /**
     * 
     * @param message
     * @param faultInfo
     */
    public ProcessWebServiceException(String message, ServiceFaultInfoSO faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param message
     * @param faultInfo
     * @param cause
     */
    public ProcessWebServiceException(String message, ServiceFaultInfoSO faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: org.kie.services.remote.ws.wsdl.ServiceFaultInfoSO
     */
    public ServiceFaultInfoSO getFaultInfo() {
        return faultInfo;
    }

}
