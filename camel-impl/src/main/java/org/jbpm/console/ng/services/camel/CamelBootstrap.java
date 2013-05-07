package org.jbpm.console.ng.services.camel;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.apache.camel.CamelContext;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.jbpm.console.ng.services.rest.DomainResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
@Startup
public class CamelBootstrap {

    private Logger logger = LoggerFactory.getLogger(CamelBootstrap.class);

    private CamelContext camelCtx;
    
    @PostConstruct
    public void init() throws Exception { 
        initializeCxfRs();
        initializeCamel();
    }
    
    public void initializeCxfRs() { 
        JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
        sf.setResourceClasses(DomainResource.class);
        sf.setAddress("http://localhost:" + CamelRouteBuilder.REST_PORT + "/");
        sf.create();
    }
    
    public void initializeCamel() throws Exception {
        CamelRouteBuilder routeBuilder = new CamelRouteBuilder();
        camelCtx = routeBuilder.getContext();

        // Add route
        camelCtx.addRoutes(new CamelRouteBuilder());

        // Go!
        camelCtx.start();
        System.out.println("--- CAMEL STARTED! --");
    }

    @PreDestroy
    public void stop() throws Exception {
        camelCtx.stop();
    }

}
