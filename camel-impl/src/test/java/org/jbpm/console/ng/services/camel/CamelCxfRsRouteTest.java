package org.jbpm.console.ng.services.camel;

import java.util.HashMap;
import java.util.Map;

import javax.naming.NamingException;
import javax.ws.rs.core.Response;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.NoErrorHandlerBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.test.AvailablePortFinder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jbpm.console.ng.services.client.api.ApiRequestFactoryProvider;
import org.jbpm.console.ng.services.client.api.MessageHolder;
import org.jbpm.console.ng.services.client.api.fluent.FluentApiRequestHandler;
import org.jbpm.console.ng.services.client.api.fluent.api.FluentKieSessionRequest;
import org.jbpm.console.ng.services.client.message.serialization.MessageSerializationProvider.Type;
import org.jbpm.console.ng.services.rest.DomainResource;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class CamelCxfRsRouteTest extends CamelTestSupport {

    private static String openEjbInitialContextFactory = "org.apache.openejb.client.LocalInitialContextFactory";
   
    @BeforeClass
    public static void setup() throws NamingException { 
        CamelRouteBuilder.SERVER_INITIAL_CONTEXT_FACTORY = openEjbInitialContextFactory;
        CamelRouteBuilder.REST_PORT = AvailablePortFinder.getNextAvailable();
        CXF_RS_ENDPOINT_URI = "cxfrs://http://localhost:" + CamelRouteBuilder.REST_PORT + "/rest?resourceClasses=" + DomainResource.class.getCanonicalName();
    }
    
    private static String CXF_RS_ENDPOINT_URI;
    
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            public void configure() {
                errorHandler(new NoErrorHandlerBuilder());
                from(CXF_RS_ENDPOINT_URI).process(new Processor() {

                    public void process(Exchange exchange) throws Exception {
                        Message inMessage = exchange.getIn();                        
                        // Get the operation name from in message
                        String operationName = inMessage.getHeader(CxfConstants.OPERATION_NAME, String.class);
                        System.out.println("--> " + operationName );
                        exchange.getOut().setBody(Response.ok().build());
                    }
                });
            }
        };
    }
    
    @Test
    public void shouldBeAbleToSendSimpleServiceMessageToProcessRequestBean() throws Exception {
        // Create message
        FluentApiRequestHandler requestFactory = ApiRequestFactoryProvider.createNewFluentApiInstance();
        requestFactory.setSerialization(Type.JAXB);
        
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", "Lin Dze");
        FluentKieSessionRequest serviceRequest = requestFactory.createKieSessionRequest().startProcess("kabuk-intrasolar", params);
        
        // Prepare POST request
        String inputXml = ((MessageHolder) serviceRequest).getMessageXmlString();
        String uriString = "/domain/session/startProcess";
        
        // Send POST request
        String output = invokePostOperXML(uriString, inputXml);
        
        // Check output
        assertTrue("Output is empty.", output != null && ! output.isEmpty());
    }
    
    @Test
    @Ignore
    public void shouldBeAbleToSendSimpleServiceMessageViaParamsToProcessRequestBean() throws Exception {
    }
    
    
    private String invokePostOperXML(String uri, String inputXml) throws Exception {
        uri = "http://localhost:" + CamelRouteBuilder.REST_PORT + "/rest" + uri;
        System.out.println("[<x] " + uri );
        
        HttpPost post = new HttpPost(uri);
        post.addHeader("Accept" , "application/xml");
        post.setEntity(new StringEntity(inputXml));
        HttpClient httpclient = new DefaultHttpClient();

        try {
            HttpResponse response = httpclient.execute(post);
            assertEquals(200, response.getStatusLine().getStatusCode());
            return EntityUtils.toString(response.getEntity());
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
    }
    
    private String invokePostOperParams(String uri, String expect) throws Exception {
        uri = "http://localhost: " + CamelRouteBuilder.REST_PORT + "/rest/" + uri;
        System.out.println("[<?] " + uri );
        
        HttpPost post = new HttpPost(uri);
        post.addHeader("Accept" , "application/xml");
        HttpClient httpclient = new DefaultHttpClient();

        try {
            HttpResponse response = httpclient.execute(post);
            assertEquals(200, response.getStatusLine().getStatusCode());
                         return EntityUtils.toString(response.getEntity());
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
    }
}
