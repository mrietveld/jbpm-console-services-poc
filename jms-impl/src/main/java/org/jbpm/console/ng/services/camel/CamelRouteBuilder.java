package org.jbpm.console.ng.services.camel;

import org.apache.camel.builder.RouteBuilder;
import org.jbpm.console.ng.services.ejb.ServerConsoleRequest;

public class CamelRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("jms:queue:JBPM.TASK")
        .convertBodyTo(ServerConsoleRequest.class)
        .to("ejb:ProcessRequestBean?method=doTaskServiceOperation");
    }


}
