package org.kie.services.remote.ws.sei.process;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetProcessInstanceInfo", propOrder = {
        "deploymentId",
        "processInstanceId"
})
public class GetProcessInstanceInfo {
    
    @XmlElement(name="deployment-id", required=true)
    @XmlSchemaType(name="string")
    private String deploymentId;
    
    @XmlElement(name="process-instance-id", required=true)
    @XmlSchemaType(name="string")
    private Long processInstanceId;

}
