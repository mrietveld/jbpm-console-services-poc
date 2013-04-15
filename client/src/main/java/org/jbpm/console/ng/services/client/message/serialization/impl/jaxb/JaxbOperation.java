package org.jbpm.console.ng.services.client.message.serialization.impl.jaxb;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;

import org.jbpm.console.ng.services.client.message.ServiceMessage.OperationMessage;

@XmlRootElement(name = "operation")
@XmlAccessorType(XmlAccessType.FIELD)
public class JaxbOperation {

    @XmlAttribute(name = "serviceType")
    @XmlSchemaType(name = "int")
    private int serviceType;

    @XmlAttribute(name = "method")
    @XmlSchemaType(name = "String")
    private String method;

    @XmlAttribute(name = "index")
    @XmlSchemaType(name = "int")
    private Integer index;

    @XmlElement(name = "result")
    private JaxbArgument result;

    @XmlElement(name = "arg")
    public List<JaxbArgument> args = new ArrayList<JaxbArgument>();

    public JaxbOperation() {
        // Default constructor
    }

    public JaxbOperation(OperationMessage origOper) {
        this.method = origOper.getMethodName();
        this.serviceType = origOper.getServiceType();
        Object[] origArgs = origOper.getArgs();
        if (origOper.isResponse()) {
            this.result = new JaxbArgument(origOper.getResult());
        } else {
            for (int i = 0; i < origArgs.length; ++i) {
                JaxbArgument arg = new JaxbArgument(origArgs[i]);
                arg.setIndex(i);
                args.add(arg);
            }
        }
    }

    public int getServiceType() {
        return serviceType;
    }

    public void setServiceType(int serviceType) {
        this.serviceType = serviceType;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public JaxbArgument getResult() {
        return result;
    }

    public void setResult(JaxbArgument result) {
        this.result = result;
    }

    public List<JaxbArgument> getArgs() {
        return args;
    }

    public void addArg(Object arg) {
        this.args.add(new JaxbArgument(arg));
    }
}
