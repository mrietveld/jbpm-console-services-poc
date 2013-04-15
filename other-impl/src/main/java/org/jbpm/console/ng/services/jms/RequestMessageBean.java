package org.jbpm.console.ng.services.jms;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.InvalidDestinationException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.log4j.Logger;
import org.jbpm.console.ng.services.cdi.MessageSerializationProviderFactory;
import org.jbpm.console.ng.services.client.message.ServiceMessage;
import org.jbpm.console.ng.services.client.message.ServiceMessage.OperationMessage;
import org.jbpm.console.ng.services.client.message.serialization.MessageSerializationProvider;
import org.jbpm.console.ng.services.ejb.ProcessRequestBean;

/**
 * This class is the link between incoming request (whether via REST or JMS or .. whatever) 
 * and the Stateless EJB that processes the requests, the {@link ProcessRequestBean}. 
 * </p>
 * Responses to requests are <b>not</b> placed on the reply-to queue, but on the corresponding answer queue. 
 * For example:<ul>
 * <li>If the request arrived on the JBPM.TASK.DOMAIN.MYCOM, then the answer would be sent to the JBPM.TAS.
 * </p>
 * Because there are multiple queues to which an instance of this class could listen to, the (JMS queue) configuration is done 
 * in the ejb-jar.xml file, which allows us to configure instances of one class to listen to more than one queue.
 */
public class RequestMessageBean implements MessageListener {

    @Inject
    private Logger logger;

    @Resource(mappedName = "java:/ConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Inject
    private ProcessRequestBean consoleProcessRequest;

    @Inject
    private MessageSerializationProviderFactory serializationProviderFactory;

    public void onMessage(Message message) {
        logger.info("Message received: " + message.toString());
        Connection connection = null;
        Session session = null;
        try {
            int serializationType = message.getIntProperty("serializationType");
            MessageSerializationProvider serializationProvider = serializationProviderFactory.getMessageSerializationProvider(serializationType);
            ServiceMessage request = serializationProvider.convertJmsMessageToServiceMessage(message);
            ServiceMessage response = new ServiceMessage(request);
            for (OperationMessage operation : request.getOperations()) {
                OperationMessage operResponse = null;

                switch (operation.getServiceType()) {
                case ServiceMessage.KIE_SESSION_REQUEST:
                    operResponse = consoleProcessRequest.doKieSessionOperation(request, operation);
                    break;
                case ServiceMessage.TASK_SERVICE_REQUEST:
                    operResponse = consoleProcessRequest.doTaskServiceOperation(request, operation);
                    break;
                default:
                    // OCRAM exception handling
                }
                
                response.addOperation(operResponse);
            }

            Message replyMessage = serializationProvider.convertServiceMessageToJmsMessage(response, session);

            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            MessageProducer producer = session.createProducer(message.getJMSReplyTo());
            producer.send(replyMessage);
        } catch (InvalidDestinationException e) {
            // OCRAM
            System.out.println("Dropping invalid message" + e.getMessage());
        } catch (Exception e) {
            // OCRAM
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
