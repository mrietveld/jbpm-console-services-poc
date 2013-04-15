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
package org.jbpm.console.ng.services;

import java.io.File;
import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ZipImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jbpm.console.ng.services.client.api.ServiceRequestFactoryProvider;
import org.jbpm.console.ng.services.client.api.fluent.FluentApiRequestFactoryImpl;
import org.jbpm.console.ng.services.client.api.remote.RemoteApiRequestFactoryImpl;
import org.jbpm.console.ng.services.client.message.serialization.MessageSerializationProvider.Type;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunAsClient
@RunWith(Arquillian.class)
public class RestIntegrationTest {

    @Deployment(testable = false)
    public static Archive<?> importDeployment() {
        String project = "other-impl";
        String version = "0.0.1-SNAPSHOT";
        String warName = project + "-" + version + ".war";
        String warPath = "target/" + warName;
        
        ZipImporter importer = ShrinkWrap.create(ZipImporter.class, warName).importFrom(new File(warPath));
        return importer.as(WebArchive.class);
    }

    @ArquillianResource
    URL deploymentUrl;

    @Test
    public void testDeployment() { 
        FluentApiRequestFactoryImpl requestFactory = getFluentRequestFactory();
        
        requestFactory.createKieSessionRequest("test");
    }

    private FluentApiRequestFactoryImpl getFluentRequestFactory() { 
        FluentApiRequestFactoryImpl factory = ServiceRequestFactoryProvider.createNewFluentApiInstance();
        factory.setSerialization(Type.JAXB);
        return factory;
    }
}
