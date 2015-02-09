
package org.scratch.ws.generated;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import org.kie.remote.services.ws.common.WebServiceFaultInfo;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.scratch.ws.generated package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _PingRequest_QNAME = new QName("http://services.ws.scratch.org/0.1.0/test", "pingRequest");
    private final static QName _PingResponse_QNAME = new QName("http://services.ws.scratch.org/0.1.0/test", "pingResponse");
    private final static QName _PingServiceException_QNAME = new QName("http://services.ws.scratch.org/0.1.0/test", "PingServiceException");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.scratch.ws.generated
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link PingRequest }
     * 
     */
    public PingRequest createPingRequest() {
        return new PingRequest();
    }

    /**
     * Create an instance of {@link PingResponse }
     * 
     */
    public PingResponse createPingResponse() {
        return new PingResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PingRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.ws.scratch.org/0.1.0/test", name = "pingRequest")
    public JAXBElement<PingRequest> createPingRequest(PingRequest value) {
        return new JAXBElement<PingRequest>(_PingRequest_QNAME, PingRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PingResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.ws.scratch.org/0.1.0/test", name = "pingResponse")
    public JAXBElement<PingResponse> createPingResponse(PingResponse value) {
        return new JAXBElement<PingResponse>(_PingResponse_QNAME, PingResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link WebServiceFaultInfo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.ws.scratch.org/0.1.0/test", name = "PingServiceException")
    public JAXBElement<WebServiceFaultInfo> createPingServiceException(WebServiceFaultInfo value) {
        return new JAXBElement<WebServiceFaultInfo>(_PingServiceException_QNAME, WebServiceFaultInfo.class, null, value);
    }

}
