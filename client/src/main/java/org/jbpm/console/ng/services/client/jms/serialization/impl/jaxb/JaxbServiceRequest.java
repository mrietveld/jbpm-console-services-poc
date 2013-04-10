package org.jbpm.console.ng.services.client.jms.serialization.impl.jaxb;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="ServiceRequest")
public class JaxbServiceRequest {

    @XmlElement(name="operation")
    public List<JaxbOperationRequest> operations;
    
}
