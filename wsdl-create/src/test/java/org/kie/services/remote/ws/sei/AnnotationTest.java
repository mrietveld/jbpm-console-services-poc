package org.kie.services.remote.ws.sei;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.jws.WebService;
import javax.xml.bind.annotation.XmlType;

import org.junit.Test;
import org.kie.services.shared.ServicesVersion;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;

public class AnnotationTest {

    private static Reflections reflections = new Reflections(
            ClasspathHelper.forPackage("org.kie.services.remote.ws.sei.command"),
            ClasspathHelper.forPackage("org.kie.services.remote.ws.sei.deployment"),
            ClasspathHelper.forPackage("org.kie.services.remote.ws.sei.history"),
            ClasspathHelper.forPackage("org.kie.services.remote.ws.sei.knowledge"),
            ClasspathHelper.forPackage("org.kie.services.remote.ws.sei.process"),
            ClasspathHelper.forPackage("org.kie.services.remote.ws.sei.task"),
            new TypeAnnotationsScanner(), 
            new FieldAnnotationsScanner(), 
            new MethodAnnotationsScanner(), 
            new SubTypesScanner());
    
    @Test
    public void namespaceVersionTest() { 
        for (Class<?> webServiceClass : reflections.getTypesAnnotatedWith(WebService.class)) {
            String className = webServiceClass.getSimpleName();
            if( className.endsWith("Impl") ) { 
                continue;
            }
           WebService wsAnno = webServiceClass.getAnnotation(WebService.class); 
           String tns = wsAnno.targetNamespace();
           assertTrue( className + " namespace does not contain " + ServicesVersion.VERSION, 
                   tns.startsWith("http://services.remote.kie.org/" + ServicesVersion.VERSION + "/") );
        }
    }

    @Test
    public void xmlTypeTest() { 
        for (Class<?> xmlTypeClass : reflections.getTypesAnnotatedWith(XmlType.class)) {
            XmlType xmlTypeAnno = xmlTypeClass.getAnnotation(XmlType.class); 
            Package pkg = xmlTypeClass.getPackage();
            
            if( ! pkg.getName().endsWith("jaxws") && ! xmlTypeClass.isEnum() ) { 
                String className = xmlTypeClass.getSimpleName();
                assertEquals( className, className, xmlTypeAnno.name() );
                Field [] fields = xmlTypeClass.getDeclaredFields();
                Set<String> props = new HashSet<String>(Arrays.asList(xmlTypeAnno.propOrder()));
                for( Field field : fields ) { 
                    String fieldName = field.getName();
                    if( fieldName.equals("serialVersionUID")) { 
                        continue;
                    }
                    assertTrue( className + "." + fieldName, props.contains(fieldName));
                }
            }
        }
    }
}