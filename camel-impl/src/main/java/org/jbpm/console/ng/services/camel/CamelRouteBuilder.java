package org.jbpm.console.ng.services.camel;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

public class CamelRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("restlet:http://localhost:4956/{id}/test").routeId("one")
        .process(new Processor() {
            public void process(Exchange exchange) throws Exception {
                exchange.getOut().setBody(
                        "received [" + exchange.getIn().getBody() + "] as an order id = " + exchange.getIn().getHeader("id"));
            }
        });
    }

}
