package org.jbpm.console.ng.services.jms;

import org.apache.camel.builder.RouteBuilder;

public class CamelRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("jms:queue:JBPM.TASK")
        .convertBodyTo(ServerConsoleRequest.class)
        .to("ejb:ConsoleProcessRequestBean?method=doTaskServiceOperation");
    }


}
