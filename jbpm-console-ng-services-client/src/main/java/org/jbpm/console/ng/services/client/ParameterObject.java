package org.jbpm.console.ng.services.client;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(namespace = "http://rest.jbpm.org/v1")
public class ParameterObject {

    @XmlAttribute
   private String key;
   
   @XmlAttribute
   private String value;
   
   public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
