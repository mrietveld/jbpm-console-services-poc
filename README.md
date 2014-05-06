# kie-remote-ws-poc

Proof of Concept (PoC) project for jBPM services via JAX-WS

The approach in this PoC and in the implementation in kie-remote will be "top-down"
or otherwise based on the WSDL (WSDL generates code), not the other way around (code
generates WSDL).

The following modules are used

- gen
  - generated classes based on wsconsume
- client
  - Contains the API and 'message' code as well as code for serialization of the messages
- impl
  - The backend server implementation
- test
  - Arquillian tests

## Package structure and code location

The code is structured as follows:

### Gen module: WSDL generated classes

- org.kie.services.ws.generated: generated classes 

### Client module: Remote Java API

- org.kie.services.client.ws

### Impl module: JAX-WS implementation

- org.kie.services.remote.ws

### Test: Arquillian integration tests 

- org.kie.services.remote.test

## Best Practices

Service characteristics:
- accessible
- documented
- robust
- reliable
- simple
- predictable

Small API's
- beware adding functionality
- small, flexible API's
- few methods as possible
- easy to use

- A Web Service should be defined with a WSDL (or WADL in case of REST) and all responses returned by the Web Service should comply with the advertised WSDL.
    - This can for example be tested with tools like SoapUI (see my blog entry Web Service Testing with soapUI) or XMLSpy, which validate SOAP responses against a WSDL.
- Use XML Schema to define the input and output of your Web Service operations.
    - Make sure to define return types for all returned data, preferably as XSD ComplexTypes.
    - Do not use the AnyType tag, as it makes it impossible (or non-beneficial) to use XML binding libraries to auto-generate code to construct request/response objects.
    - For ComplexTypes, make sure the occurrence indicators (minOccurs and maxOccurs) are correctly defined, so that it is clear to the consumer which fields are required and which are optional and the frequency of each field.
    - Make sure your namespaces are well defined (not some default like "org.tempuri")
- Do not use a proprietary authentication protocol for your Web Service.  Rather use common standards like HttpAuth or Kerberos.  Or define username/password as part of your XML payload and expose your Web Service via SSL.
- Keep it simple  
    - Write a Web Service with many simple methods rather than one large method (with numerous arguments) that tries to be everything to everyone.
    - Adhere to the OO principles of maximizing cohesion and minimizing coupling when designing your Web Services.
- Make sure your Web Service returns error messages that are useful for debugging/tracking problems.  For example, include an error code and a human readable error description.
- Make sure to offer a development environment for your service, which preferably runs the same Web Service version as production, but off of a test database rather than production data.
- Thoroughly test your Web Service, in a technology-agnostic manner, before having others integrate with it.

