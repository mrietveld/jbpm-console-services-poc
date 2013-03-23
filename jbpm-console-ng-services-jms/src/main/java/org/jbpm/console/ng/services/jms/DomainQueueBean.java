package org.jbpm.console.ng.services.jms;

import java.io.PrintStream;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.hornetq.api.core.management.ObjectNameBuilder;
import org.hornetq.api.jms.management.JMSServerControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
@Startup
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class DomainQueueBean {

    private Logger logger = LoggerFactory.getLogger(TaskMessageBean.class);

    private List<String> queueList = new ArrayList<String>();

    @PostConstruct
    public void createDomainQueues() {
        try {
            MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
            ObjectName on = ObjectNameBuilder.DEFAULT.getJMSServerObjectName();
            
            String queueName = "JBPM.TASK.DOMAIN";
            String jndiBindings = "queue/"+queueName+",java:jboss/exported/jms/queue/" + queueName;
            Object [] params = { queueName, jndiBindings, null, true };
            String [] signature = { 
                    String.class.getName(), // name
                    String.class.getName(), // jndiBindings
                    String.class.getName(), // selector
                    boolean.class.getName() // durable
            };
            mBeanServer.invoke(on, "createQueue", params, signature);

        } catch (Exception e) {
            logger.error("Unable to perform operations via " + JMSServerControl.class.getSimpleName(), e);
        }
    }

    private void printMBeanInfo(MBeanInfo mbi, PrintStream out) {
        System.out.println(mbi.getClassName());
        MBeanAttributeInfo[] mbas = mbi.getAttributes();
        for (MBeanAttributeInfo mba : mbas) {
            System.out.println("attr: " + mba.getName() + " of type " + mba.getType());
        }

        MBeanOperationInfo[] mbos = mbi.getOperations();
        for (MBeanOperationInfo mbo : mbos) {
            System.out.println("oper: " + mbo.getName());
            MBeanParameterInfo[] mbps = mbo.getSignature();
            for (MBeanParameterInfo mbp : mbps) {
                System.out.println("  param: " + mbp.getName() + " : " + mbp.getType());
            }

            System.out.println("   returns: " + mbo.getReturnType());
        }

    }

}
