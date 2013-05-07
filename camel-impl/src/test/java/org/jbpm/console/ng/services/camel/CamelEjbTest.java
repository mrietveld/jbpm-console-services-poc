package org.jbpm.console.ng.services.camel;

import static org.junit.Assert.*;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.jbpm.console.ng.services.ejb.TestEjbBean;
import org.junit.Before;
import org.junit.Test;

public class CamelEjbTest {

    private InitialContext initialContext;

    private String openEjbInitialContextFactory = "org.apache.openejb.client.LocalInitialContextFactory";
    
    @Before
    public void setUp() throws Exception {
        Properties properties = new Properties();
        properties.setProperty(Context.INITIAL_CONTEXT_FACTORY, openEjbInitialContextFactory);
        
        CamelRouteBuilder.SERVER_INITIAL_CONTEXT_FACTORY = openEjbInitialContextFactory;
        CamelRouteBuilder.REST_PORT = 8080;
        
        initialContext = new InitialContext(properties);
    }

    @Test
    public void testGreaterViaLocalInterface() throws Exception {
        Object object = initialContext.lookup("TestLocalBean");

        assertNotNull(object);
        assertTrue(object instanceof TestEjbBean);
        TestEjbBean testBean = (TestEjbBean) object;

        assertEquals("String", testBean.print("World"));
    }
    
}
