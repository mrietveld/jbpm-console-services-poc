package org.kie.services.remote.ws;

import javax.jws.WebService;

import org.kie.services.remote.ws.wsdl.VersionSO;
import org.kie.services.remote.ws.wsdl.generated.HelloWebService;
import org.kie.services.remote.ws.wsdl.generated.HelloWebServiceException;
import org.kie.services.remote.ws.wsdl.generated.MessageSO;
import org.kie.services.remote.ws.wsdl.generated.NameSO;
import org.kie.services.remote.ws.wsdl.generated.SayHelloInputSO;
import org.kie.services.remote.ws.wsdl.generated.SayHelloOutputSO;

@WebService(
        serviceName = "HelloService", 
        portName = "HelloServicePort", 
        name = "HelloService", 
        endpointInterface = "org.kie.services.remote.ws.wsdl.generated.HelloWebService",
        targetNamespace = "http://services.test.org/hello")
public class HelloWebServiceImpl implements HelloWebService {

    public final static String ENDPOINT_INTERFACE = HelloWebService.class.getName();
    
    @Override
    public SayHelloOutputSO sayHello(SayHelloInputSO input) throws HelloWebServiceException {
        String msg = "Hello " + input.getYourName();
        System.out.println(msg);
        SayHelloOutputSO out = new SayHelloOutputSO();
        out.setGreeting(msg);
        return out;
    }

    @Override
    public MessageSO sayHelloRefSO(NameSO arg0) throws HelloWebServiceException {
        // DBG Auto-generated method stub
        return null;
    }

    @Override
    public VersionSO version() throws HelloWebServiceException {
        // DBG Auto-generated method stub
        return null;
    }

}
