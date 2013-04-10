package org.jbpm.console.ng.services.client.jms.serialization.impl.jaxb;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.junit.Test;
import org.kie.api.runtime.KieSession;

public class SimpleJaxbTest {

    @Test
    public void showXML() throws Exception {
        JAXBContext jaxbCtx = JAXBContext.newInstance(JaxbServiceRequest.class);

        JaxbServiceRequest request = new JaxbServiceRequest();
        request.operations = new ArrayList<JaxbOperationRequest>();

        JaxbOperationRequest operation = new JaxbOperationRequest();
        request.operations.add(operation);

        operation.method = "myMethod";
        operation.serviceClass = KieSession.class;
        operation.args = new ArrayList<JaxbMethodArgument>();

        JaxbMethodArgument arg = new JaxbMethodArgument();
        operation.args.add(arg);

        List<String> og = new ArrayList<String>();
        og.add("asdf");
        arg.index = 0;
        arg.type = (og).getClass();
        arg.content = convertToByteArray(og);

        Marshaller marshaller = jaxbCtx.createMarshaller();
        marshaller.marshal(request, System.out);
    }

    private byte[] convertToByteArray(Object input) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oout = new ObjectOutputStream(baos);
        oout.writeObject(input);
        return baos.toByteArray();
    }
}
