//package org.jbpm.console.ng.services;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.URL;
//import java.util.Properties;
//
//import javax.jms.Connection;
//import javax.jms.ConnectionFactory;
//import javax.jms.MapMessage;
//import javax.jms.Message;
//import javax.jms.MessageConsumer;
//import javax.jms.MessageProducer;
//import javax.jms.Queue;
//import javax.jms.Session;
//import javax.jms.TemporaryQueue;
//import javax.naming.InitialContext;
//import javax.naming.NamingException;
//
//import org.jboss.arquillian.container.test.api.Deployment;
//import org.jboss.arquillian.container.test.api.RunAsClient;
//import org.jboss.arquillian.junit.Arquillian;
//import org.jboss.arquillian.test.api.ArquillianResource;
//import org.jboss.as.arquillian.api.ServerSetup;
//import org.jboss.shrinkwrap.api.Archive;
//import org.jboss.shrinkwrap.api.ShrinkWrap;
//import org.jboss.shrinkwrap.api.importer.ZipImporter;
//import org.jboss.shrinkwrap.api.spec.WebArchive;
//import org.jbpm.console.ng.services.setup.ArquillianJbossServerSetupTask;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
///**
// * This is a simple test that tests whether the war can be succesfully deployed on WildFly (AS 7).
// */
//@RunAsClient
//@RunWith(Arquillian.class)
//@ServerSetup(ArquillianJbossServerSetupTask.class)
//public class JmsIntegrationTest {
//
//    private static Logger logger = LoggerFactory.getLogger(JmsIntegrationTest.class);
//
//    private static final String CONNECTION_FACTORY_NAME = "jms/RemoteConnectionFactory";
//    private static final String DOMAIN_TASK_QUEUE_NAME = "jms/queue/JBPM.TASK.DOMAIN.TEST";
//    private static final String TASK_QUEUE_NAME = "jms/queue/JBPM.TASK";
//
//    /**
//     * Reads the properties for a (remote) InitialContext from the (filtered
//     * src/test/resources/)initalContext.properties file and intializes a
//     * (remote) IntialContext instance.
//     * 
//     * @return a remote {@link InitialContext} instance
//     */
//    private static InitialContext getRemoteInitialContext() {
//        String initalPropertiesFileName = "/initialContext.properties";
//        Properties properties = new Properties();
//        try {
//            InputStream initialContextProperties = JmsIntegrationTest.class.getResourceAsStream(initalPropertiesFileName);
//            properties.load(initialContextProperties);
//        } catch (IOException e) {
//            throw new RuntimeException("Unable to read " + initalPropertiesFileName, e);
//        }
//        for (Object keyObj : properties.keySet()) {
//            String key = (String) keyObj;
//            System.setProperty(key, (String) properties.get(key));
//        }
//        try {
//            return new InitialContext(properties);
//        } catch (NamingException e) {
//            throw new RuntimeException("Unable to create " + InitialContext.class.getSimpleName(), e);
//        }
//    }
//
//    @Deployment(testable = false)
//    public static Archive<?> importDeployment() {
//        String project = "other-impl";
//        String version = "0.0.1-SNAPSHOT";
//        String warName = project + "-" + version + ".war";
//        String warPath = "target/" + warName;
//        
//        ZipImporter importer = ShrinkWrap.create(ZipImporter.class, warName).importFrom(new File(warPath));
//        return importer.as(WebArchive.class);
//    }
//
//    private static final long QUALITY_OF_SERVICE_THRESHOLD_MS = 5 * 1000;
//
//    @ArquillianResource
//    public URL serverUrl;
//
//    @Test
//    public void shouldBeAbleToGetMessageBack() throws Exception {
//        InitialContext context = getRemoteInitialContext();
//        ConnectionFactory factory = (ConnectionFactory) context.lookup(CONNECTION_FACTORY_NAME);
//        Queue jbpmQueue = (Queue) context.lookup(TASK_QUEUE_NAME);
//
//        Connection connection = null;
//        Session session = null;
//        try {
//            connection = factory.createConnection("guest", "1234");
//            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//
//            TemporaryQueue tempQueue = session.createTemporaryQueue();
//            MessageProducer producer = session.createProducer(jbpmQueue);
//            MessageConsumer consumer = session.createConsumer(tempQueue);
//
//            connection.start();
//
//          
//            MapMessage requestMap = null;
//            
//            requestMap.setJMSReplyTo(tempQueue);
//
//            producer.send(requestMap);
//            Message response = consumer.receive(QUALITY_OF_SERVICE_THRESHOLD_MS);
//            assertNotNull("Response from MDB was null!", response);
//            
//            String responseBody = null;
//
//            // assertEquals("Should have responded with same message", info[0], responseBody);
//        } finally {
//            if (connection != null) {
//                connection.close();
//            }
//        }
//    }
//
//    @Test
//    public void shouldBeAbleToGetMessageBackFromDomainQueue() throws Exception {
//        InitialContext context = getRemoteInitialContext();
//        ConnectionFactory factory = (ConnectionFactory) context.lookup(CONNECTION_FACTORY_NAME);
//        Queue jbpmQueue = (Queue) context.lookup(DOMAIN_TASK_QUEUE_NAME);
//
//        Connection connection = null;
//        Session session = null;
//        try {
//            connection = factory.createConnection("guest", "1234");
//            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//
//            TemporaryQueue tempQueue = session.createTemporaryQueue();
//            MessageProducer producer = session.createProducer(jbpmQueue);
//            MessageConsumer consumer = session.createConsumer(tempQueue);
//
//            connection.start();
//
//            String [] info = { "domain", "23", "org.kie.api.runtime.KieSession", "startProcessInstance" };
//            
//
//            MapMessage requestMap = null;
//            
//            requestMap.setJMSReplyTo(tempQueue);
//
//            producer.send(requestMap);
//            Message response = consumer.receive(QUALITY_OF_SERVICE_THRESHOLD_MS);
//            assertNotNull("Response from MDB was null!", response);
//            String responseBody = null;
//
//            assertEquals("Should have responded with same message", info[0], responseBody);
//        } finally {
//            if (connection != null) {
//                connection.close();
//            }
//        }
//    }
//}