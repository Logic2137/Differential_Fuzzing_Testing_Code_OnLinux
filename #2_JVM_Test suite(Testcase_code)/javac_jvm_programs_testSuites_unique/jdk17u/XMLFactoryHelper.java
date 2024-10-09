
package test;

import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;
import java.lang.Class;
import java.lang.String;
import java.lang.System;
import java.util.Iterator;
import java.util.ServiceLoader;
import org.xml.sax.helpers.XMLReaderFactory;

public class XMLFactoryHelper {

    public static Object instantiateXMLService(String serviceName) throws Exception {
        ClassLoader backup = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(XMLFactoryHelper.class.getClassLoader());
            if (serviceName.equals("org.xml.sax.XMLReader"))
                return XMLReaderFactory.createXMLReader();
            else if (serviceName.equals("javax.xml.validation.SchemaFactory"))
                return Class.forName(serviceName).getMethod("newInstance", String.class).invoke(null, W3C_XML_SCHEMA_NS_URI);
            else
                return Class.forName(serviceName).getMethod("newInstance").invoke(null);
        } finally {
            Thread.currentThread().setContextClassLoader(backup);
        }
    }
}
