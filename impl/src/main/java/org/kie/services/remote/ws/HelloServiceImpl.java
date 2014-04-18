package org.kie.services.remote.ws;

import javax.jws.WebService;

import org.kie.services.remote.ws.wsdl.VersionSO;
import org.kie.services.remote.ws.wsdl.generated.MessageSO;
import org.kie.services.remote.ws.wsdl.generated.NameSO;
import org.kie.services.remote.ws.wsdl.generated.SayHelloInputSO;
import org.kie.services.remote.ws.wsdl.generated.SayHelloOutputSO;

@WebService(serviceName = "HelloService", portName = "HelloServicePort", name = "HelloService", 
    endpointInterface = "org.kie.services.remote.ws.wsdl.generated.HelloService",
    targetNamespace = "http://services.test.org/hello")
public class HelloServiceImpl implements HelloService {

    @Override
    public SayHelloOutputSO sayHello(SayHelloInputSO input) throws HelloServiceException {
       System.out.println( "Hello " + input.getYourName() );
        return null;
    }

    @Override
    public MessageSO sayHelloRefSO(NameSO arg0) throws HelloServiceException {
        // DBG Auto-generated method stub
        return null;
    }

    @Override
    public VersionSO version() throws HelloServiceException {
        // DBG Auto-generated method stub
        return null;
    }

}
