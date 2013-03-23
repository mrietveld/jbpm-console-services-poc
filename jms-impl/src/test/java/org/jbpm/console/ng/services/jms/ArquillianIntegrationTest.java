package org.jbpm.console.ng.services.jms;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TemporaryQueue;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.as.arquillian.api.ServerSetup;
import org.jboss.as.arquillian.api.ServerSetupTask;
import org.jboss.as.arquillian.container.ManagementClient;
import org.jboss.as.controller.client.helpers.standalone.DeploymentPlan;
import org.jboss.as.controller.client.helpers.standalone.ServerDeploymentActionResult;
import org.jboss.as.controller.client.helpers.standalone.ServerDeploymentManager;
import org.jboss.as.controller.client.helpers.standalone.ServerDeploymentPlanResult;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jbpm.console.ng.services.client.jms.ConsoleRequestFactory;
import org.jbpm.console.ng.services.client.jms.ConsoleRequestHolder;
import org.jbpm.console.ng.services.jms.dynamic.DynamicQueueCreationBean;
import org.jbpm.console.ng.services.jms.mdb.KnowledgeSessionMessageBean;
import org.jbpm.console.ng.services.jms.mdb.TaskMessageBean;
import org.jbpm.console.ng.services.shared.DomainRuntimeManagerProvider;
import org.jbpm.console.ng.services.shared.DomainRuntimeManagerProviderImpl;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.api.runtime.KieSession;
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
@ServerSetup(ArquillianIntegrationTest.MessageBeanTestSetup.class)
public class ArquillianIntegrationTest {

    private Logger logger = LoggerFactory.getLogger(ArquillianIntegrationTest.class);

    private static Properties arquillianLaunchProperties = getArquillianLaunchProperties();

    private static final String CONNECTION_FACTORY_NAME = "jms/RemoteConnectionFactory";
    private static final String TASK_QUEUE_NAME = "jms/queue/JBPM.TASK";
    private static final String KSESSION_QUEUE_NAME = "jms/queue/JBPM.SESSION";

    /**
     * This method reads the arquillian.launch file in src/test/resources --
     * well, actually, it reads the one that has already been processed and
     * placed in the build directory. </p> Depending on the maven profile being
     * run, this file is filled with the name of the arquillian profile (in the
     * arquillian.xml file) that should be used to run the test. </p> The
     * project is setup this way so that we can run tests against both jboss and
     * glassfish(-embedded).
     * 
     * @return A {@link Properties} object with one key, the name of the
     *         arquillian profile being used.
     */
    public static Properties getArquillianLaunchProperties() {
        Properties properties = new Properties();
        try {
            InputStream arquillianLaunchFile = ArquillianIntegrationTest.class.getResourceAsStream("/arquillian.launch");
            properties.load(arquillianLaunchFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties;
    }

    /**
     * This is a Arquillian {@link ServerSetupTask} class. </p> This class is
     * used to deploy and create the JMS queues that both this test and the main
     * classes in this module expect to exist in the application server. </p>
     * HOWEVER, the DomainQueueBean currently takes care of creating the needed
     * queue upon startup! Which is why this class is no longer referenced and
     * the @ServerSetup anno has been commented out.
     */
    public static class MessageBeanTestSetup implements ServerSetupTask {

        public static final String HORNETQ_JMS_XML = "/hornetq-jms.xml";

        @Override
        public void setup(ManagementClient managementClient, String containerId) throws Exception {
            System.out.println("DEPLOY");

            if (arquillianLaunchProperties.containsKey("glassfish-embedded")) {
                throw new UnsupportedOperationException("Glassfish JMS Queue creation not yet supported.");
            } else if (arquillianLaunchProperties.containsKey("jboss")) {
                URL hornetqJmsXmlUrl = ArquillianIntegrationTest.class.getResource(HORNETQ_JMS_XML);

                ServerDeploymentManager manager = ServerDeploymentManager.Factory.create(managementClient.getControllerClient());

                DeploymentPlan plan = manager.newDeploymentPlan().add(hornetqJmsXmlUrl).andDeploy().build();
                Future<ServerDeploymentPlanResult> future = manager.execute(plan);
                ServerDeploymentPlanResult result = future.get(10, TimeUnit.SECONDS);
                ServerDeploymentActionResult actionResult = result.getDeploymentActionResult(plan.getId());
                if (actionResult != null) {
                    if (actionResult.getDeploymentException() != null) {
                        throw new RuntimeException(actionResult.getDeploymentException());
                    }
                }
            } else {
                arquillianLaunchProperties.store(System.err, null);
                throw new RuntimeException("Unknown arquillian launch container.");
            }
        }

        @Override
        public void tearDown(ManagementClient managementClient, String containerId) throws Exception {
            ServerDeploymentManager manager = ServerDeploymentManager.Factory.create(managementClient.getControllerClient());
            DeploymentPlan undeployPlan = manager.newDeploymentPlan().undeploy(HORNETQ_JMS_XML).andRemoveUndeployed().build();
            manager.execute(undeployPlan).get();
        }

    }

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

    /**
     * Method to create the artifact to be deployed to the server.
     * 
     * @return
     */
    @Deployment(testable = false)
    public static Archive<?> createDeployment() {

        File[] libs = Maven
                .resolver()
                .loadPomFromFile("pom.xml")
                .resolve("org.hornetq:hornetq-core-client", 
                        "org.hornetq:hornetq-jms-client", 
                        "org.apache.camel:camel-jms",
                        "org.apache.camel:camel-ejb",
                        "org.apache.camel:camel-cdi",
                        "org.jbpm:jbpm-console-ng-services-client",
                        "org.jbpm:jbpm-console-ng-services-shared",
                        "org.jbpm:jbpm-persistence-jpa"
                        ).withTransitivity().asFile();

        return ShrinkWrap.create(WebArchive.class)
                .addClasses(CamelBootstrap.class, CamelRouteBuilder.class)
                .addClasses(ServerConsoleRequest.class, ConsoleProcessRequestBean.class)
                .addClasses(DomainRuntimeManagerProvider.class, DomainRuntimeManagerProviderImpl.class)
                .addAsResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsLibraries(libs);
    }

    private static final long QUALITY_OF_SERVICE_THRESHOLD_MS = 5 * 1000;

    @ArquillianResource
    public URL serverUrl;

    @Test
    public void shouldBeAbleToSendMessage() throws Exception {
        InitialContext context = getRemoteInitialContext();
        ConnectionFactory factory = (ConnectionFactory) context.lookup(CONNECTION_FACTORY_NAME);
        Queue jbpmQueue = (Queue) context.lookup(TASK_QUEUE_NAME);

        KieSession kieSessionRequest = ConsoleRequestFactory.createNewInstance().createConsoleKieSessionRequest("domain");
        kieSessionRequest.abortProcessInstance(18);
            
        Connection connection = null;
        Session session = null;
        try {
            connection = factory.createConnection("guest", "1234");
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            MessageProducer producer = session.createProducer(jbpmQueue);
            connection.start();

            Message request = ((ConsoleRequestHolder) kieSessionRequest).createMessage(session);
            producer.send(request);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    @Test
    @Ignore
    public void shouldBeAbleToGetMessageBack() throws Exception {
        InitialContext context = getRemoteInitialContext();
        ConnectionFactory factory = (ConnectionFactory) context.lookup(CONNECTION_FACTORY_NAME);
        Queue jbpmQueue = (Queue) context.lookup(TASK_QUEUE_NAME);

        String messageBody = "ping";

        Connection connection = null;
        Session session = null;
        try {
            connection = factory.createConnection("guest", "1234");
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            TemporaryQueue tempQueue = session.createTemporaryQueue();
            MessageProducer producer = session.createProducer(jbpmQueue);
            MessageConsumer consumer = session.createConsumer(tempQueue);

            connection.start();

            Message request = session.createTextMessage(messageBody);
            request.setJMSReplyTo(tempQueue);

            producer.send(request);
            Message response = consumer.receive(QUALITY_OF_SERVICE_THRESHOLD_MS);
            assertNotNull("Response from MDB was null!", response);
            String responseBody = ((TextMessage) response).getText();

            assertEquals("Should have responded with same message", messageBody, responseBody);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

}