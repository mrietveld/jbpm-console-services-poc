package org.kie.services.remote.ws.test;

import static org.junit.Assert.*;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.jws.WebService;
import javax.xml.namespace.QName;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.Asset;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.webapp30.WebAppDescriptor;
import org.jboss.shrinkwrap.descriptor.api.webcommon30.WebAppVersionType;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.services.remote.ws.HelloServiceImpl;
import org.kie.services.remote.ws.wsdl.generated.HelloService;
import org.kie.services.remote.ws.wsdl.generated.HelloServiceClient;
import org.kie.services.remote.ws.wsdl.generated.SayHelloInputSO;
import org.kie.services.remote.ws.wsdl.generated.SayHelloOutputSO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunAsClient
@RunWith(Arquillian.class)
public class HelloServiceTomcatIntegrationTest {
    
    private static final Logger logger = LoggerFactory.getLogger(HelloServiceTomcatIntegrationTest.class);
    
    @Deployment(testable = false, name="helloservice-ws")
    public static Archive<?> createDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class, "test.war")
                .addClasses(HelloServiceImpl.class)
                .setWebXML(generateWebXMLAsset())
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
      
        File [] pocDeps = Maven.resolver()
                .loadPomFromFile("pom.xml")
                .resolve("org.kie.remote:jax-ws-poc-gen",
                         "org.jboss.spec.javax.servlet:jboss-servlet-api_3.0_spec",
                         "org.jboss.spec.javax.servlet.jsp:jboss-jsp-api_2.2_spec")
                .withTransitivity()
                .asFile();
        war.addAsLibraries(pocDeps);
        
        File [] wsDeps = Maven.resolver()
                .loadPomFromFile("pom.xml")
                .resolve("org.jboss.ws.native:jbossws-native-core")
                .withTransitivity()
                .asFile();
        war.addAsLibraries(wsDeps);
        
        return war;
    }

    private static Asset generateWebXMLAsset() {
        WebAppDescriptor wad = Descriptors.create(WebAppDescriptor.class);

        wad.version(WebAppVersionType._3_0.toString())
        /**
        .createServlet()
        .servletName("HelloService")
        .servletClass(HelloServiceImpl.class.getName())
        .up()
        .createServletMapping()
        .servletName("HelloService")
        .urlPattern("/*")
        .up()
        **/
        ;
                
        logger.debug("---\n" + wad.exportAsString() + "---\n");
        return new StringAsset(wad.exportAsString());
    }
    
    @ArquillianResource
    URL deploymentUrl;
    
    private static final String WSDL_PATH = "HelloService?wsdl";
    
    private HelloServiceClient client;
   
    @Before
    public void setup() {
        try {
            client = new HelloServiceClient(
                    new URL(deploymentUrl, WSDL_PATH),
                    getServiceQName());
        } catch (MalformedURLException murle) {
            String msg = "Unable to create service client: " + murle.getMessage();
            logger.error( msg, murle);
            fail( msg );
        }
    }

    private static QName getServiceQName() { 
        WebService wsAnno = HelloService.class.getAnnotation(WebService.class);
        String namespace = wsAnno.targetNamespace();
        String name = wsAnno.name();
        return new QName(namespace, name);
    }
    
    @Test
    public void testHelloWebService() throws Exception {
        logger.info("[Client] Requesting the WebService to say Hello.");

        // Get a response from the WebService
        SayHelloInputSO input = new SayHelloInputSO();
        input.setYourName("Eefje");
        final SayHelloOutputSO response = client.getHelloServicePort().sayHello(input);

        logger.info("[WebService] " + response);
    }
}