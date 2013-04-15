package org.jbpm.console.ng.services.setup;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.jboss.as.arquillian.api.ServerSetupTask;
import org.jboss.as.arquillian.container.ManagementClient;
import org.jbpm.console.ng.services.JMSIntegrationTest;
import org.jbpm.console.ng.services.setup.jboss.DataSourceServerSetupTask;
import org.jbpm.console.ng.services.setup.jboss.JBossServerSetupTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArquillianServerSetupTask implements ServerSetupTask { 

    protected static Properties arquillianLaunchProperties = getArquillianLaunchProperties();
    
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
    protected static Properties getArquillianLaunchProperties() {
        Properties properties = new Properties();
        try {
            InputStream arquillianLaunchFile = JMSIntegrationTest.class.getResourceAsStream("/arquillian.launch");
            properties.load(arquillianLaunchFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties;
    }
    
    @Override
    public void setup(ManagementClient managementClient, String containerId) throws Exception {
        LoggerFactory.getLogger(this.getClass()).info("Deploying...");
        if (arquillianLaunchProperties.containsKey("jboss")) {
            JBossServerSetupTask.setupServer(managementClient, containerId);
        } else if (arquillianLaunchProperties.containsKey("glassfish-embedded")) {
            throw new UnsupportedOperationException("Glassfish server setup not yet supported.");
        } else {
            arquillianLaunchProperties.store(System.err, null);
            throw new RuntimeException("Unknown arquillian launch container.");
        }
    }

    @Override
    public void tearDown(ManagementClient managementClient, String containerId) throws Exception {
        if (arquillianLaunchProperties.containsKey("jboss")) {
            JBossServerSetupTask.cleanUpServer(managementClient, containerId);
        } else if (arquillianLaunchProperties.containsKey("glassfish-embedded")) {
            throw new UnsupportedOperationException("Glassfish server clean-up not yet supported.");
        } else {
            arquillianLaunchProperties.store(System.err, null);
            throw new RuntimeException("Unknown arquillian launch container.");
        }
    }

}
