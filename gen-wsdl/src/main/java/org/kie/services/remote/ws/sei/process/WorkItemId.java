package org.kie.services.remote.ws.sei.process;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

import org.kie.services.remote.ws.objects.SerializableServiceObject;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WorkItemId", propOrder = {
    "deploymentId",
    "workItemId"
})
public class WorkItemId extends SerializableServiceObject {

    /** generated Serial Version UID */
    private static final long serialVersionUID = -9207348385851998251L;

    @XmlElement(required=true)
    @XmlSchemaType(name="string")
    private String deploymentId;
    
    @XmlElement(required=true)
    @XmlSchemaType(name="int")
    private Integer workItemId;
    
}
