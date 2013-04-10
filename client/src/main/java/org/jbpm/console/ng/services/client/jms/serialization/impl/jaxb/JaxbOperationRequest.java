package org.jbpm.console.ng.services.client.jms.serialization.impl.jaxb;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;

@XmlRootElement(name="operation")
public class JaxbOperationRequest {

    @XmlAttribute(name="service")
    @XmlSchemaType(name="string")
    public Class serviceClass;
    
    @XmlAttribute(name="method")
    public String method;
    
    public List<JaxbMethodArgument> args;
}
