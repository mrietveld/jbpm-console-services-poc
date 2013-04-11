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

import org.jbpm.console.ng.services.client.jms.ServiceRequest;
import org.jbpm.console.ng.services.client.jms.ServiceRequest.OperationRequest;
import org.jbpm.console.ng.services.client.jms.ServiceResponse;
import org.jbpm.console.ng.services.client.jms.ServiceResponse.OperationResponse;
import org.jbpm.console.ng.services.client.jms.serialization.MessageSerializationProvider;
import org.jbpm.console.ng.services.ejb.ProcessRequestBean;
import org.slf4j.Logger;

/**
 * This class is the link between incoming request (whether via REST or JMS or .. whatever) 
 * and the Stateless EJB that processes the requests, the {@link ProcessRequestBean}. 
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
    private MessageSerializationProvider serializationProvider;

    public void onMessage(Message message) {
        logger.info("Message received: " + message.toString());
        Connection connection = null;
        Session session = null;
        try {
            ServiceRequest request = serializationProvider.convertMessageToServerRequest(message);
            ServiceResponse response = new ServiceResponse(request);
            for (OperationRequest operation : request.getOperations()) {
                OperationResponse operResponse = null;

                switch (operation.getServiceType()) {
                case ServiceRequest.KIE_SESSION_REQUEST:
                    operResponse = consoleProcessRequest.doKieSessionOperation(request, operation);
                    break;
                case ServiceRequest.TASK_SERVICE_REQUEST:
                    operResponse = consoleProcessRequest.doTaskServiceOperation(request, operation);
                    break;
                default:
                    // OCRAM exception handling
                }

                response.getResponses().add(operResponse);
            }

            Message replyMessage = serializationProvider.convertResponseToMessage(response);

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
