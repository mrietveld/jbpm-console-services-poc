package org.jbpm.console.ng.services.client.jms.serialization.impl.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;

@XmlRootElement(name="argument")
@XmlAccessorType(XmlAccessType.FIELD)
public class JaxbArgument {

    @XmlAttribute(name="index")
    @XmlSchemaType(name="int")
    private Integer index;
    
    @XmlAttribute(name="type")
    @XmlSchemaType(name="string")
    private Class<?> type;
    
    @XmlElement(name="content")
    @XmlSchemaType(name="base64Binary")
    private Object content;
    
    public JaxbArgument() { 
        // Default constructor
    }
    
    public JaxbArgument(Object arg) { 
        this.type = arg.getClass();
        this.content = arg;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Class<?> getType() {
        return type;
    }

    public Object getContent() {
        return content;
    }
}
