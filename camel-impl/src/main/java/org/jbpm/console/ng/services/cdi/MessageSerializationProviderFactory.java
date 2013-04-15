package org.jbpm.console.ng.services.cdi;

import javax.enterprise.context.ApplicationScoped;

import org.jbpm.console.ng.services.client.message.serialization.MessageSerializationProvider;
import org.jbpm.console.ng.services.client.message.serialization.MessageSerializationProvider.Type;
import org.jbpm.console.ng.services.client.message.serialization.impl.MapMessageSerializationProvider;
import org.jbpm.console.ng.services.client.message.serialization.impl.ProtobufSerializationProvider;
import org.jbpm.console.ng.services.client.message.serialization.impl.jaxb.JaxbSerializationProvider;

@ApplicationScoped
public class MessageSerializationProviderFactory {

    private final MapMessageSerializationProvider mapMessageSerialization = new MapMessageSerializationProvider();
    private final JaxbSerializationProvider jaxbSerialization = new JaxbSerializationProvider();
    private final ProtobufSerializationProvider protobufSerialization = new ProtobufSerializationProvider();
    
    /**
     * See {@link Type} for the type to int mapping. 
     * @param serializationType
     * @return
     */
    public MessageSerializationProvider getMessageSerializationProvider(int serializationType) { 
        switch(serializationType) { 
        case 0: 
            return mapMessageSerialization;
        case 1: 
            return jaxbSerialization;
        case 2: 
            return protobufSerialization;
        default: 
            throw new UnsupportedOperationException("Unknown serialization type: " + serializationType );
        }
    }
}
