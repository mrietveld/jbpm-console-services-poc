package org.jbpm.console.ng.services.client.message.serialization.impl.jaxb;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;


public abstract class JaxbArgument {

    @XmlAttribute(name="index")
    @XmlSchemaType(name="int")
    private Integer index;
    
    public Integer getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
    
}
