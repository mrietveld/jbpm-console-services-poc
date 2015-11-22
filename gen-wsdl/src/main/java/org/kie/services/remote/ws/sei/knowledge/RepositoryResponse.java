package org.kie.services.remote.ws.sei.knowledge;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RepositoryResponse", propOrder = {
    "userName",
    "requestType",
    "gitURL"
})
public class RepositoryResponse {

    private String userName;
    private String requestType;
    private String gitURL;
    
}
