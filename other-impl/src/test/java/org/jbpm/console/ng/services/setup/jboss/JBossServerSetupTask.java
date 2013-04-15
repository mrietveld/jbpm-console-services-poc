package org.jbpm.console.ng.services.setup.jboss;

import java.net.URL;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.jboss.as.arquillian.api.ServerSetupTask;
import org.jboss.as.arquillian.container.ManagementClient;
import org.jboss.as.controller.client.helpers.standalone.DeploymentPlan;
import org.jboss.as.controller.client.helpers.standalone.ServerDeploymentActionResult;
import org.jboss.as.controller.client.helpers.standalone.ServerDeploymentManager;
import org.jboss.as.controller.client.helpers.standalone.ServerDeploymentPlanResult;
import org.jbpm.console.ng.services.JMSIntegrationTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a Arquillian {@link ServerSetupTask} class.
 * </p>
 * This class is used to deploy/create the following:
 * <ul>
 * <li>the JMS queues (in <code>hornetq-jms.xml</code>)</li>
 * <li>the jdbc datasource (in <code>jbpm-ds.xml</code>)</li>
 * </ul>
 * Both this test and the main classes in this module expect these
 * to exist on the application server.
 */

public class JBossServerSetupTask {

    private static Logger logger = LoggerFactory.getLogger(JMSIntegrationTest.class);

    public static final String HORNETQ_JMS_XML = "/hornetq-jms.xml";
    public static final String JBPM_DS_XML = "/jbpm-ds.xml";

    public static void setupServer(ManagementClient managementClient, String containerId) throws Exception {
        logger.info("Deploying JMS Queues");

        URL hornetqJmsXmlUrl = JMSIntegrationTest.class.getResource(HORNETQ_JMS_XML);
        URL jbpmDSXmlUrl = JMSIntegrationTest.class.getResource(JBPM_DS_XML);

        URL[] urls = { hornetqJmsXmlUrl, jbpmDSXmlUrl };

        for (URL url : urls) {
            ServerDeploymentManager manager = ServerDeploymentManager.Factory.create(managementClient.getControllerClient());
            DeploymentPlan plan = manager.newDeploymentPlan().add(url).andDeploy().build();

            Future<ServerDeploymentPlanResult> future = manager.execute(plan);
            ServerDeploymentPlanResult result = future.get(10, TimeUnit.SECONDS);
            ServerDeploymentActionResult actionResult = result.getDeploymentActionResult(plan.getId());
            if (actionResult != null) {
                if (actionResult.getDeploymentException() != null) {
                    throw new RuntimeException(actionResult.getDeploymentException());
                }
            }
        }
    }

    public static void cleanUpServer(ManagementClient managementClient, String containerId) throws Exception {
        ServerDeploymentManager manager = ServerDeploymentManager.Factory.create(managementClient.getControllerClient());
        DeploymentPlan undeployPlan = manager.newDeploymentPlan().undeploy(HORNETQ_JMS_XML).undeploy(JBPM_DS_XML)
                .andRemoveUndeployed().build();

        manager.execute(undeployPlan).get();
    }

}
