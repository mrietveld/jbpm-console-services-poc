package org.jbpm.console.ng.services;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.as.arquillian.api.ServerSetup;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ZipImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jbpm.console.ng.services.setup.ArquillianServerSetupTask;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This test is purely to figure out how to get everything deployed.. 
 * 
 */
@RunAsClient
@RunWith(Arquillian.class)
@ServerSetup(ArquillianServerSetupTask.class)
public class ArquillianIntegrationTest extends Assert {

    private static Logger logger = LoggerFactory.getLogger(ArquillianIntegrationTest.class);

    /**
     * Reads the properties for a (remote) InitialContext from the (filtered
     * src/test/resources/)initalContext.properties file and intializes a
     * (remote) IntialContext instance.
     * 
     * @return a remote {@link InitialContext} instance
     */
    private static InitialContext getRemoteInitialContext() {
        String initalPropertiesFileName = "/initialContext.properties";
        Properties properties = new Properties();
        try {
            InputStream initialContextProperties = ArquillianIntegrationTest.class.getResourceAsStream(initalPropertiesFileName);
            properties.load(initialContextProperties);
        } catch (IOException e) {
            throw new RuntimeException("Unable to read " + initalPropertiesFileName, e);
        }
        for (Object keyObj : properties.keySet()) {
            String key = (String) keyObj;
            System.setProperty(key, (String) properties.get(key));
        }
        try {
            return new InitialContext(properties);
        } catch (NamingException e) {
            throw new RuntimeException("Unable to create " + InitialContext.class.getSimpleName(), e);
        }
    }

    @Deployment(testable = false)
    public static Archive<?> importDeployment() {
        String project = "test-impl";
        String version = "0.0.1-SNAPSHOT";
        String warPath = "target/" + project + "-" + version + ".war";
        
        ZipImporter importer = ShrinkWrap.create(ZipImporter.class, "test-jbpm-services.war").importFrom(new File(warPath));
        return importer.as(WebArchive.class);
    }


    @ArquillianResource
    public URL serverUrl;

    @Test
    public void shouldBeAbleToDeploy() throws Exception {
        assertTrue(serverUrl != null);
    }

}