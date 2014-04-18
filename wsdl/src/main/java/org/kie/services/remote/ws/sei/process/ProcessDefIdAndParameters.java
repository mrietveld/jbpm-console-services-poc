package org.kie.services.remote.ws.sei.process;

import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProcessDefIdAndParameters", propOrder = {
    "deploymentId",
    "processDefinitionId",
    "parameters"
})
public class ProcessDefIdAndParameters {

    @XmlElement(name="deployment-id", required=true)
    @XmlSchemaType(name="string")
    private String deploymentId;
    
    @XmlElement(name="process-definition-id", required=true)
    @XmlSchemaType(name="string")
    private String processDefinitionId;

    @XmlElement
    private Map<String, Object> parameters;
    
}
