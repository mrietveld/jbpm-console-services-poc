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

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URL;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ZipImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jbpm.console.ng.services.client.api.MessageHolder;
import org.jbpm.console.ng.services.client.api.ServiceRequestFactoryProvider;
import org.jbpm.console.ng.services.client.api.fluent.FluentApiRequestFactoryImpl;
import org.jbpm.console.ng.services.client.api.fluent.api.FluentKieSessionRequest;
import org.jbpm.console.ng.services.client.message.serialization.MessageSerializationProvider.Type;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.restlet.data.MediaType;

@RunAsClient
@RunWith(Arquillian.class)
public class RestIntegrationTest {

    public static final String APPLICATION_XML = "application/xml";
    
    @Deployment(testable = false)
    public static Archive<?> importDeployment() {
        String project = "other-impl";
        String version = "0.0.1-SNAPSHOT";
        String warName = project + "-" + version + ".war";
        String warPath = "target/" + warName;
        
        ZipImporter importer = ShrinkWrap.create(ZipImporter.class, "arquillian.war").importFrom(new File(warPath));
        return importer.as(WebArchive.class);
    }

    @ArquillianResource
    URL deploymentUrl;

    @Test
    public void shouldBeAbleToDeployAndProcessSimpleRestRequest() throws Exception { 
        FluentApiRequestFactoryImpl requestFactory = getFluentRequestFactory();

        // create service request
        FluentKieSessionRequest serviceRequest = requestFactory.createKieSessionRequest();
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("user-id", "Lin Dze");
        serviceRequest.startProcess("test-process", params);
        
        String msgXmlString = ((MessageHolder) serviceRequest).getMessageJaxbXml();

        // create REST request
        String urlString = new URL(deploymentUrl, "test" + "/session/startProcess").toExternalForm();
        System.out.println( ">> " + urlString );
        
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost postRequest = new HttpPost(urlString);
        postRequest.setEntity(new StringEntity(msgXmlString));
        postRequest.setHeader("Accept", APPLICATION_XML);
        postRequest.setHeader("Content-type", APPLICATION_XML);

        // Get response
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        HttpResponse response = httpClient.execute(postRequest);
        
        // Check response
        assertEquals(200, response.getStatusLine().getStatusCode());
        HttpEntity resultEntity = response.getEntity();
        String resultString = null;
        if( resultEntity != null ) { 
            resultString = EntityUtils.toString(resultEntity);
        }
        System.out.println("OUT: " + resultString);
    }

    private FluentApiRequestFactoryImpl getFluentRequestFactory() { 
        FluentApiRequestFactoryImpl factory = ServiceRequestFactoryProvider.createNewFluentApiInstance();
        factory.setSerialization(Type.JAXB);
        return factory;
    }
}
