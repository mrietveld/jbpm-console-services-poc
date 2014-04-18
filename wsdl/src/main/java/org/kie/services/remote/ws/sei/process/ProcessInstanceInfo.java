package org.kie.services.remote.ws.sei.process;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProcessInstanceInfo", propOrder = {
    "id",
    "state",
    "processId",
    "eventTypes"
})
public class ProcessInstanceInfo {
    
    @XmlElement(required=true)
    @XmlSchemaType(name="long")
    private Long id;

    @XmlElement(required=true)
    @XmlSchemaType(name="int")
    private Integer state; 

    @XmlElement(name="process-id", required=true)
    @XmlSchemaType(name="string")
    private String processId;

    @XmlElement(name="event-types")
    private List<String> eventTypes = new ArrayList<String>();
}
