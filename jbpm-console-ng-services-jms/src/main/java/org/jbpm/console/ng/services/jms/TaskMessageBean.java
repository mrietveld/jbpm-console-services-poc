package org.jbpm.console.ng.services.jms;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.InvalidDestinationException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@MessageDriven(activationConfig = { @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "java:/queue/JBPM.TASK.DOMAIN") })
public class TaskMessageBean implements MessageListener {

    private Logger logger = LoggerFactory.getLogger(TaskMessageBean.class);

    @Resource(mappedName = "java:/ConnectionFactory")
    private ConnectionFactory connectionFactory;

    public void onMessage(Message message) {
        respond(message);
    }

    private void respond(Message message) {
        logger.info("Message received: " + message.toString());
        Connection connection = null;
        Session session = null;
        try {
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer producer = session.createProducer(message.getJMSReplyTo());
            producer.send(message);
        } catch (InvalidDestinationException e) {
            System.out.println("Dropping invalid message" + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Could not reply to message", e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                    session.close();
                } catch (Exception e) {
                }
            }
        }
    }

}
