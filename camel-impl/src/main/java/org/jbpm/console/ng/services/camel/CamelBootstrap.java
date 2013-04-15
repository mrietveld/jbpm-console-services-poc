package org.jbpm.console.ng.services.camel;

import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.camel.CamelContext;
import org.apache.camel.component.ejb.EjbComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
@Startup
public class CamelBootstrap {

    private Logger logger = LoggerFactory.getLogger(CamelBootstrap.class);

    private CamelContext camelCtx;
    
    @PostConstruct
    public void initializeCamel() throws Exception {
        camelCtx = new DefaultCamelContext();
        EjbComponent ejb = camelCtx.getComponent("ejb", EjbComponent.class);
        ejb.setContext(createInitialContext());

        // Add route
        camelCtx.addRoutes(new CamelRouteBuilder());

        // Go!
        camelCtx.start();
        System.out.println("--- CAMEL STARTED! --");
    }

    private Context createInitialContext() throws NamingException {
        Properties properties = new Properties();
        properties.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.as.naming.InitialContextFactory");

        return new InitialContext(properties);
    }

    @PreDestroy
    public void stop() throws Exception {
        camelCtx.stop();
    }

}
