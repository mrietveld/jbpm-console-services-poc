package org.jbpm.console.ng.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TemporaryQueue;
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
import org.jbpm.console.ng.services.shared.MapMessageEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * In order for this test to succeed:
 * <ul>
 * <li>With jBoss: you must add
 * <ul>
 * <code>&lt;jmx-management-enabled&gt;true&lt;/jmx-management-enabled&gt;</code>
 * </ul>
 * to the <code>&lt;hornetq-server&gt;</code> part of
 * <ul>
 * <code>target/jboss-as-${jboss.version}/standalone/configuration/standalone-full.xml</code>
 * </ul>
 * </li>
 * <li>With GlassFish: ???</li>
 * </ul>
 * 
 * 
 */
@RunAsClient
@RunWith(Arquillian.class)
@ServerSetup(ArquillianServerSetupTask.class)
public class ArquillianIntegrationTest {

    private static Logger logger = LoggerFactory.getLogger(ArquillianIntegrationTest.class);

    private static final String CONNECTION_FACTORY_NAME = "jms/RemoteConnectionFactory";
    private static final String DOMAIN_TASK_QUEUE_NAME = "jms/queue/JBPM.TASK.DOMAIN.TEST";
    private static final String TASK_QUEUE_NAME = "jms/queue/JBPM.TASK";

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
        String project = "other-impl";
        String version = "0.0.1-SNAPSHOT";
        String warPath = "target/" + project + "-" + version + ".war";
        
        ZipImporter importer = ShrinkWrap.create(ZipImporter.class, "test-jms.war").importFrom(new File(warPath));
        return importer.as(WebArchive.class);
    }

    private static final long QUALITY_OF_SERVICE_THRESHOLD_MS = 5 * 1000;

    @ArquillianResource
    public URL serverUrl;

    @Test
    public void shouldBeAbleToGetMessageBack() throws Exception {
        InitialContext context = getRemoteInitialContext();
        ConnectionFactory factory = (ConnectionFactory) context.lookup(CONNECTION_FACTORY_NAME);
        Queue jbpmQueue = (Queue) context.lookup(TASK_QUEUE_NAME);

        Connection connection = null;
        Session session = null;
        try {
            connection = factory.createConnection("guest", "1234");
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            TemporaryQueue tempQueue = session.createTemporaryQueue();
            MessageProducer producer = session.createProducer(jbpmQueue);
            MessageConsumer consumer = session.createConsumer(tempQueue);

            connection.start();

            String [] info = { "domain", "23", "org.kie.api.runtime.KieSession", "startProcessInstance" };
            
            MapMessage requestMap = session.createMapMessage();
            requestMap.setString(MapMessageEnum.DomainName.toString(), info[0] );
            requestMap.setString(MapMessageEnum.KieSessionId.toString(), info[1] );
            requestMap.setString(MapMessageEnum.ObjectClass.toString(), info[2] );
            requestMap.setString(MapMessageEnum.MethodName.toString(), info[3] );
            
            requestMap.setJMSReplyTo(tempQueue);

            producer.send(requestMap);
            Message response = consumer.receive(QUALITY_OF_SERVICE_THRESHOLD_MS);
            assertNotNull("Response from MDB was null!", response);
            String responseBody = ((MapMessage) response).getString(MapMessageEnum.DomainName.toString() );

            assertEquals("Should have responded with same message", info[0], responseBody);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    @Test
    public void shouldBeAbleToGetMessageBackFromDomainQueue() throws Exception {
        InitialContext context = getRemoteInitialContext();
        ConnectionFactory factory = (ConnectionFactory) context.lookup(CONNECTION_FACTORY_NAME);
        Queue jbpmQueue = (Queue) context.lookup(DOMAIN_TASK_QUEUE_NAME);

        Connection connection = null;
        Session session = null;
        try {
            connection = factory.createConnection("guest", "1234");
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            TemporaryQueue tempQueue = session.createTemporaryQueue();
            MessageProducer producer = session.createProducer(jbpmQueue);
            MessageConsumer consumer = session.createConsumer(tempQueue);

            connection.start();

            String [] info = { "domain", "23", "org.kie.api.runtime.KieSession", "startProcessInstance" };
            
            MapMessage requestMap = session.createMapMessage();
            requestMap.setString(MapMessageEnum.DomainName.toString(), info[0] );
            requestMap.setString(MapMessageEnum.KieSessionId.toString(), info[1] );
            requestMap.setString(MapMessageEnum.ObjectClass.toString(), info[2] );
            requestMap.setString(MapMessageEnum.MethodName.toString(), info[3] );
            
            requestMap.setJMSReplyTo(tempQueue);

            producer.send(requestMap);
            Message response = consumer.receive(QUALITY_OF_SERVICE_THRESHOLD_MS);
            assertNotNull("Response from MDB was null!", response);
            String responseBody = ((MapMessage) response).getString(MapMessageEnum.DomainName.toString() );

            assertEquals("Should have responded with same message", info[0], responseBody);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }
}