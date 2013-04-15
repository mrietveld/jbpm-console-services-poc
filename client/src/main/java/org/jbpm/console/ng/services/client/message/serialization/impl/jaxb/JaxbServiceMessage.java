package org.jbpm.console.ng.services.client.message.serialization.impl.jaxb;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;

import org.jbpm.console.ng.services.client.message.ServiceMessage;
import org.jbpm.console.ng.services.client.message.ServiceMessage.OperationMessage;

@XmlRootElement(name="message")
@XmlAccessorType(XmlAccessType.FIELD)
public class JaxbServiceMessage {

    @XmlElement
    @XmlSchemaType(name="string")
    private String domain; 
    
    @XmlElement
    @XmlSchemaType(name="string")
    private String sessionId; 
    
    @XmlElement(name="operation")
    private List<JaxbOperation> operations = new ArrayList<JaxbOperation>();
    
    public JaxbServiceMessage() { 
        // Default constructor
    }
    
    public JaxbServiceMessage(ServiceMessage origRequest) { 
       this.domain = origRequest.getDomainName();
       if( origRequest.getSessionId() != null ) { 
           this.sessionId = String.valueOf( origRequest.getSessionId() );
       }
       this.operations = new ArrayList<JaxbOperation>();
       
       for( OperationMessage oper : origRequest.getOperations() ) { 
           JaxbOperation jaxbOper = new JaxbOperation(oper);
           operations.add(jaxbOper);
       }
    }
    
    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    // DELETE ME
    public void addOperation(JaxbOperation oper) {
        this.operations.add(oper);
    }
    
    public void addOperation(OperationMessage origOper) {
        this.operations.add(new JaxbOperation(origOper));
    }

    public List<JaxbOperation> getOperation() { 
        return this.operations;
    }
    
}
