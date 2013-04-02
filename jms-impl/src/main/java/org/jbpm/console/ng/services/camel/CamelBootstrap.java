package org.jbpm.console.ng.services.camel;

import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.jms.ConnectionFactory;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cdi.CdiCamelContext;
import org.apache.camel.component.ejb.EjbComponent;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.util.jndi.JndiContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
@Startup
public class CamelBootstrap {

    private Logger logger = LoggerFactory.getLogger(CamelBootstrap.class);
    
    @Resource(name="java:/ConnectionFactory")
    private ConnectionFactory connectionFactory;
   
    @Inject
    CdiCamelContext camelCtx;
    
    @Inject 
    CamelRouteBuilder camelRoute;
    
    @PostConstruct
    public void initializeCamel() throws Exception {
        EjbComponent ejb = camelCtx.getComponent("ejb", EjbComponent.class);
        ejb.setContext(createInitialContext());
        
        // Add route
        camelCtx.addRoutes(camelRoute);
        
        // Go!
        camelCtx.start();
    }

    private Context createInitialContext() throws NamingException {
        int i = 0;
        String sep = "-";
        while( i++ < 8 ) { 
            sep = sep + sep;
        }
        System.out.println( "SYSTEM PROPS: ");
        System.out.println( sep );
        System.getProperties().list(System.out);
        System.out.println( sep );
        
        Properties properties = new Properties();
        if (false) {
            properties.setProperty(Context.INITIAL_CONTEXT_FACTORY, "com.sun.enterprise.naming.SerialInitContextFactory");
            properties.setProperty(Context.URL_PKG_PREFIXES, "com.sun.enterprise.naming");
            properties.setProperty(Context.STATE_FACTORIES, "com.sun.corba.ee.impl.presentation.rmi.JNDIStateFactoryImpl");
        } else {
           properties.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.as.naming.InitialContext.Factory");
        }

        return new InitialContext(properties);
    }
    
    @PreDestroy
    public void stop() throws Exception {
       camelCtx.stop();
    }

}
