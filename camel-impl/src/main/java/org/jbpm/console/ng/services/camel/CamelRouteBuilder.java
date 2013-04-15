package org.jbpm.console.ng.services.camel;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.ejb.EjbComponent;

public class CamelRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
     // enlist EJB component using the JndiContext
        EjbComponent ejb = getContext().getComponent("ejb", EjbComponent.class);
        ejb.setContext(createEjbContext());
        
        from("restlet:http://localhost/{id}/session/startProcess").routeId("one")
        .to("ejb:TestBean?method=print");
    }
    
    public void restRouteConfigure() throws Exception { 
        
    }

    private static Context createEjbContext() throws NamingException {
        Properties properties = new Properties();
        
        if( false ) { // glassfish
            properties.setProperty(Context.INITIAL_CONTEXT_FACTORY, "com.sun.enterprise.naming.SerialInitContextFactory");
            properties.setProperty(Context.URL_PKG_PREFIXES, "com.sun.enterprise.naming");
            properties.setProperty(Context.STATE_FACTORIES, "com.sun.corba.ee.impl.presentation.rmi.JNDIStateFactoryImpl");
        } else if( true ) { // jboss
            properties.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.as.naming.InitialContextFactory");
        }

        return new InitialContext(properties);
      }
}
