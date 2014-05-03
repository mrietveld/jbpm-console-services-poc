package org.kie.services.remote.ws.sei.task;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProcessInstanceId", propOrder = {
    "deploymentId",
    "processInstanceId"
})
public class TaskOperationRequest {

    @XmlElement(required=true)
    private TaskOperationType type;
    
    @XmlElement(required=false)
    @XmlSchemaType(name="string")
    private String targetEntityId;
    
    @XmlElement(required=false)
    @XmlSchemaType(name="string")
    private String language;
   
    // For nominate
    @XmlElement(required=false, name="user")
    @XmlSchemaType(name="string")
    private List<String> users;
    
    @XmlElement(required=false, name="group")
    @XmlSchemaType(name="string")
    private List<String> groups;
    
}
