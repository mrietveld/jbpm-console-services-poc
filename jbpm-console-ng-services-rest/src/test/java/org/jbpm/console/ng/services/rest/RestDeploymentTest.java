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
package org.jbpm.console.ng.services.rest;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.runner.RunWith;

@RunAsClient
@RunWith(Arquillian.class)
public class RestDeploymentTest {

    @Deployment(testable = false)
    public static WebArchive getTestArchive() {
        Properties properties = new Properties();
        try {
            InputStream arquillianLaunchFile = RestDeploymentTest.class.getResourceAsStream("/arquillian.launch");
            properties.load(arquillianLaunchFile);
        } catch (IOException e) {
            // do nada
        }

        WebArchive war = null;
        if (properties.containsKey("glassfish-embedded")) {
            war = ShrinkWrap.create(WebArchive.class)
                    .addPackages(true, AbstractRuntimeResource.class.getPackage()) 
                    .addAsResource("persistence.xml", "META-INF/persistence.xml") // persistence
                    .addAsWebInfResource("WEB-INF/web.xml", "web.xml");
        } else if (properties.containsKey("jbossas-7")) {
            war = ShrinkWrap.create(WebArchive.class)
                    .addPackages(true, AbstractRuntimeResource.class.getPackage())
                    // servlet
                    .addAsResource("persistence.xml", "META-INF/persistence.xml")
                    .addAsWebInfResource("WEB-INF/web.xml", "web.xml")
                    // jboss specific (because it's not embedded.. :( )
                    .addAsWebInfResource("jbossas-ds.xml");
        } else {
            properties.list(System.out);
            throw new RuntimeException("No property specifying which arquillian container to start.");
        }

        return war;
    }

    @ArquillianResource
    URL deploymentUrl;

  
}
