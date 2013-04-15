/*
 * JBoss, Home of Professional Open Source
 * 
 * Copyright 2012, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jbpm.console.ng.services.jms;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.importer.ZipImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jbpm.console.ng.services.camel.CamelBootstrap;
import org.jbpm.console.ng.services.camel.CamelRouteBuilder;
import org.jbpm.console.ng.services.ejb.TestEJBBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.restlet.engine.http.header.WarningWriter;

@RunAsClient
@RunWith(Arquillian.class)
public class RestDeploymentTest {

    @Deployment(testable = false, name = "jbpm-rest.war")
    public static Archive<?> getTestArchive() {
        return createWar();
    }

    private static Archive<?> createWar() {
        Properties properties = new Properties();
        try {
            InputStream arquillianLaunchFile = RestDeploymentTest.class.getResourceAsStream("/arquillian.launch");
            properties.load(arquillianLaunchFile);
        } catch (IOException e) {
            // do nada
        }

        File[] libs = Maven
                .resolver()
                .loadPomFromFile("pom.xml")
                .resolve("org.apache.camel:camel-restlet", 
                        "org.apache.camel:camel-ejb"
                        // "org.restlet.jee:org.restlet.ext.servlet"
                        ).withTransitivity().asFile();

        for (File lib : libs) {
            System.out.println(lib.getName());
        }

        WebArchive war = null;
        if (properties.containsKey("glassfish-embedded")) {
            war = ShrinkWrap.create(WebArchive.class, "test-rest.war").addClasses(CamelBootstrap.class, CamelRouteBuilder.class)
                    .addClasses(TestEJBBean.class).addAsWebInfResource("WEB-INF/web.xml", "web.xml");
        } else if (properties.containsKey("jboss")) {
            war = ShrinkWrap.create(WebArchive.class).addClasses(CamelBootstrap.class, CamelRouteBuilder.class)
                    .addClasses(TestEJBBean.class)
                    // dependencies
                    .addAsLibraries(libs)
                    // servlet
                    .addAsWebInfResource("WEB-INF/web.xml", "web.xml")
                    .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
        } else {
            properties.list(System.out);
            throw new RuntimeException("No property specifying which arquillian container to start.");
        }

        return war;
    }

    private static Archive<?> getWar() {
        String project = "rest-impl";
        String version = "0.0.1-SNAPSHOT";
        String warName = project + "-" + version + ".war";
        String warPath = "target/" + warName;

        ZipImporter importer = ShrinkWrap.create(ZipImporter.class, warName).importFrom(new File(warPath));
        return importer.as(WebArchive.class);
    }

    @ArquillianResource
    URL deploymentUrl;

    @Test
    public void testRestDeployment() throws Exception {
        URL testUrl = new URL(deploymentUrl.getProtocol(), deploymentUrl.getHost(), 4956, "/test");
        System.out.println(testUrl.toString());

        String urlString = testUrl .toExternalForm();
        System.out.println("> " + urlString);

        ClientRequest request = new ClientRequest(urlString);
        request.setHttpMethod("POST");

        // we're expecting a String back
        ClientResponse<String> responseObj = request.get(String.class);

        assertEquals(200, responseObj.getStatus());
        String result = responseObj.getEntity();
        System.out.println(result);
    }

}
