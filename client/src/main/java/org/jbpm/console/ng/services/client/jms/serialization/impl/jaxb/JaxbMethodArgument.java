package org.jbpm.console.ng.services.client.jms.serialization.impl.jaxb;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;

@XmlRootElement(name="argument")
public class JaxbMethodArgument {

    @XmlAttribute(name="index")
    public Integer index;
    
    @XmlAttribute(name="type")
    @XmlSchemaType(name="string")
    public Class<?> type;
    
    @XmlElement(name="content")
    @XmlSchemaType(name="base64Binary")
    public Object content;
}
