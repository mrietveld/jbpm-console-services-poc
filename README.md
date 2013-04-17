# jbpm-console-services-poc

Proof of Concept (PoC) project for jBPM services via JMS and REST.

The following modules are the important modules: 

- client
  - Contains the API and 'message' code as well as code for serialization of the messages
- other-impl
  - A non-camel implemenation of REST and JMS api's
- camel-impl
  - A camel implemenation of REST and JMS api's

## Package structure and code location

The code is structured as follows:

### Client module: dependency for clients to use

- org.jbpm.console.ng.services.client
  - api: classes to be used by the user in order to create the message
    - fluent: (remote) fluent api implementation
    - remote: (non-fluent) remote api implementation
    - same: "api" implementation based on the existing KieSession, WorkItemManager and TaskService interfaces
  - message: classes that contain the basic message/DTO objects as well as serializatoin
    - serialization: serialization classes
      - impl: different serialization implementations
        - jaxb: completed jaxb serialization code

### Other-Impl: RestEasy and MDB (non-camel) implementation

- org.jbpm.console.ng.services
  - cdi: various producers that are necessary (persistence, logging, etc.)
  - ejb: Contains the ProcessRequestBean, which does the real work of processing a request
  - jms: Contains the RequestMessageBean which processes JMS queues
  - rest: Contains the DomainResourceBean which processes REST requests

### Camel-Impl: camel based implementation

- org.jbpm.console.ng.services
  - camel: contains the camel configuration/initialization code
  - cdi: various producers that are necessary (persistence, logging, etc.)
  - ejb: Contains the ProcessRequestBean, which does the real work of processing a request

## Use of a Proxy mechanism

To start with, regardless of whether we end up using a fluent (remote) Api, a non-fluent remote api
or simply the existing kie api/interfaces, the code is structured such that it actually doesn't 
matter which set of interfaces we use. 

In other words, the methods defined in the interface that defines the request decide the following: 
- The methods available in the request object. 
- Which operations are available via the REST object. 
- Which methods are available via the JMS interface. 

I'll simplify this even more, with a concrete example: 

```java
// create service request
FluentApiRequestFactoryImpl requestFactory = getFluentRequestFactory();
FluentKieSessionRequest serviceRequest = requestFactory.createKieSessionRequest();

// fill service request
HashMap<String, Object> params = new HashMap<String, Object>();
params.put("user-id", "Lin Dze");
serviceRequest.startProcess("test-process", params);

String msgXmlString = ((MessageHolder) serviceRequest).getMessageJaxbXml();

// create REST request
String urlString = new URL(deploymentUrl, "testDomain" + "/session/startProcess").toExternalForm();
System.out.println( ">> " + urlString );
    
ClientRequest restRequest = new ClientRequest(urlString);
restRequest.body(MediaType.APPLICATION_XML, msgXmlString);

// send and response
ClientResponse<String> responseObj = restRequest.post(String.class);
```

In the example above, I'm sending a `startProcess` command (with some parameters) to the
REST interface. However, in the all of the code -- both on the client and server side -- needed
to call this method, the word "startProcess" is only found in the `FluentKieSessionRequeset`
interface. Unfortunately, in this case, the user also needs to specify "startProcess" in the URL, 
although she could also have used the general "testDomain/session/operation" method instead. 

