package org.jbpm.console.ng.services.camel;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Response;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.builder.NoErrorHandlerBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.component.ejb.EjbComponent;
import org.jbpm.console.ng.services.UnfinishedError;
import org.jbpm.console.ng.services.rest.DomainResource;

public class CamelRouteBuilder extends RouteBuilder {

    static int REST_PORT = 80;
    static String SERVER_INITIAL_CONTEXT_FACTORY = "org.jboss.as.naming.InitialContextFactory";

    private String[] domainResourceMethodNames;

    // Default constructor
    public CamelRouteBuilder() {
        Method[] resourceMethods = DomainResource.class.getMethods();
        domainResourceMethodNames = new String[resourceMethods.length];

        for (int i = 0; i < resourceMethods.length; ++i) {
            domainResourceMethodNames[i] = resourceMethods[i].getName();
        }

        /**
         * 0: kieSessionOperationWithParams
         * 1: kieSessionOperationWithXml
         * 2: processInstanceOperationWithParams
         * 3: processInstanceOperationWithXml
         * 4: processInstanceStatus
         * 5: taskOperationWithParams
         * 6: taskOperationWithXml
         */
        Arrays.sort(domainResourceMethodNames);
    }
    
    private static final int KIE_SESSION_PARAM = 0;
    private static final int KIE_SESSION_XML = 1;
    private static final int PROC_INST_PARAM = 2;
    private static final int PROC_INST_XML = 3;
    private static final int PROC_INST_STATUS = 4;
    private static final int TASK_SERVICE_PARAM = 5;
    private static final int TASK_SERVICE_XML = 6;

    @Override
    public void configure() throws Exception {
        // enlist EJB component using the JndiContext
        EjbComponent ejb = getContext().getComponent("ejb", EjbComponent.class);
        ejb.setContext(createEjbContext());

        testRestRouteConfigure();
    }

    private String getCxfRsEndpointUri() {
        return "cxfrs://http://localhost:" + REST_PORT + "/rest?resourceClasses=" + DomainResource.class.getCanonicalName();
    }

    private void testRestRouteConfigure() throws Exception {
        from(getCxfRsEndpointUri()).process(new Processor() {
            public void process(Exchange exchange) throws Exception { 
                exchange.getOut().setBody("Hello!");
            }
        });
    }
    
    private void restRouteConfigure() throws Exception {
        errorHandler(new NoErrorHandlerBuilder());
        
        from(getCxfRsEndpointUri()).process(new Processor() {

            public void process(Exchange exchange) throws Exception {
                Message inMessage = exchange.getIn();
                // Get the operation name from in message
                String operationName = inMessage.getHeader(CxfConstants.OPERATION_NAME, String.class);
                if (domainResourceMethodNames[KIE_SESSION_XML].equals(operationName)) { 
                    String path = inMessage.getHeader(Exchange.HTTP_PATH, String.class);

                    // The parameter of the invocation is stored in the body of in message
                    String id = inMessage.getBody(String.class);
                    if ("/customerservice/customers/126".equals(path)) {
                        // We just put the response Object into the out message body
                        org.apache.cxf.message.Message cxfMessage = inMessage.getHeader(CxfConstants.CAMEL_CXF_MESSAGE,
                                org.apache.cxf.message.Message.class);
                        Response r = Response.status(200).entity("The remoteAddress is " + " [blah blah]").build();
                        exchange.getOut().setBody(r);
                        return;
                    } else {
                        throw new RuntimeCamelException("Can't found the customer with uri " + path);
                    }
                } else if( domainResourceMethodNames[KIE_SESSION_PARAM].equals(operationName) ) {
                    throw new UnfinishedError(domainResourceMethodNames[KIE_SESSION_PARAM] + " not yet implemented.");
                } else if( domainResourceMethodNames[PROC_INST_XML].equals(operationName) ) {
                    throw new UnfinishedError(domainResourceMethodNames[PROC_INST_XML] + " not yet implemented.");
                } else if( domainResourceMethodNames[PROC_INST_PARAM].equals(operationName) ) {
                    throw new UnfinishedError(domainResourceMethodNames[PROC_INST_PARAM] + " not yet implemented.");
                } else if( domainResourceMethodNames[PROC_INST_STATUS].equals(operationName) ) {
                    throw new UnfinishedError(domainResourceMethodNames[PROC_INST_STATUS] + " not yet implemented.");
                } else if( domainResourceMethodNames[TASK_SERVICE_XML].equals(operationName) ) {
                    throw new UnfinishedError(domainResourceMethodNames[TASK_SERVICE_XML] + " not yet implemented.");
                } else if( domainResourceMethodNames[TASK_SERVICE_PARAM].equals(operationName) ) {
                    throw new UnfinishedError(domainResourceMethodNames[TASK_SERVICE_PARAM] + " not yet implemented.");
                }
            }

        });
    }

    private static Context createEjbContext() throws NamingException {
        Properties properties = new Properties();

        if (false) { // glassfish
            properties.setProperty(Context.INITIAL_CONTEXT_FACTORY, "com.sun.enterprise.naming.SerialInitContextFactory");
            properties.setProperty(Context.URL_PKG_PREFIXES, "com.sun.enterprise.naming");
            properties.setProperty(Context.STATE_FACTORIES, "com.sun.corba.ee.impl.presentation.rmi.JNDIStateFactoryImpl");
        } else if (true) { // jboss
            properties.setProperty(Context.INITIAL_CONTEXT_FACTORY, SERVER_INITIAL_CONTEXT_FACTORY);
        }

        return new InitialContext(properties);
    }
}
