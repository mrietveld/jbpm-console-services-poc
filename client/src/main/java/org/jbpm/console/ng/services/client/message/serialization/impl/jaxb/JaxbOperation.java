package org.jbpm.console.ng.services.client.message.serialization.impl.jaxb;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
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

    @XmlElementRef(name="args")
    public List<JaxbArgument> args = new ArrayList<JaxbArgument>();

    public JaxbOperation() {
        // Default constructor
    }

    public JaxbOperation(OperationMessage origOper) {
        this.method = origOper.getMethodName();
        this.serviceType = origOper.getServiceType();
        Object[] origArgs = origOper.getArgs();
        if (origOper.isResponse()) {
            this.result = convertToJaxbArgument(origOper.getResult());
        } else {
            for (int i = 0; i < origArgs.length; ++i) {
                JaxbArgument arg = convertToJaxbArgument(origArgs[i]);
                arg.setIndex(i);
                args.add(arg);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private JaxbArgument convertToJaxbArgument(Object obj) {
        JaxbArgument arg = null;
        if (obj instanceof Map) {
            arg = new JaxbMap((Map<String, Object>) obj);
        } else {
            arg = new JaxbSingleArgument(obj);
        }
        return arg;
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

    public void addArg(Object obj) {
        JaxbArgument arg = convertToJaxbArgument(obj);
        arg.setIndex(args.size());
        args.add(arg);
    }
}
