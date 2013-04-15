package org.jbpm.console.ng.services.client.api;

import org.jbpm.console.ng.services.client.message.serialization.MessageSerializationProvider;
import org.jbpm.console.ng.services.client.message.serialization.impl.MapMessageSerializationProvider;
import org.jbpm.console.ng.services.client.message.serialization.impl.ProtobufSerializationProvider;
import org.jbpm.console.ng.services.client.message.serialization.impl.jaxb.JaxbSerializationProvider;

public class AbstractApiRequestFactoryImpl {

    protected MessageSerializationProvider serializationProvider = null;
   
    public void setSerializationProvider(MessageSerializationProvider serializationProvider) {
        this.serializationProvider = serializationProvider;
    }

    public void setSerialization(MessageSerializationProvider.Type serializationType) {
        switch (serializationType) {
        case JAXB:
            this.serializationProvider = new JaxbSerializationProvider();
            break;
        case MAP_MESSAGE:
            this.serializationProvider = new MapMessageSerializationProvider();
            break;
        case PROTOBUF:
            this.serializationProvider = new ProtobufSerializationProvider();
            break;
        default:
            throw new UnsupportedOperationException("Unknown serialization type: " + serializationType.toString());
        }
    }
}
