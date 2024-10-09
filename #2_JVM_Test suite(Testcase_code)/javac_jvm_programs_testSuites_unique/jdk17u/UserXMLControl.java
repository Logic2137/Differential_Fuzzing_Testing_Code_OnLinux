
package com.foo;

import java.io.*;
import java.net.*;
import java.util.*;
import static java.util.ResourceBundle.Control.*;

public class UserXMLControl extends ResourceBundle.Control {

    @Override
    public List<String> getFormats(String baseName) {
        if (baseName == null) {
            throw new NullPointerException();
        }
        return Arrays.asList("xml");
    }

    @Override
    public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload) throws IllegalAccessException, InstantiationException, IOException {
        if (baseName == null || locale == null || format == null || loader == null) {
            throw new NullPointerException();
        }
        ResourceBundle bundle = null;
        if (format.equals("xml")) {
            String bundleName = toBundleName(baseName, locale);
            String resourceName = toResourceName(bundleName, format);
            URL url = loader.getResource(resourceName);
            if (url != null) {
                URLConnection connection = url.openConnection();
                if (connection != null) {
                    if (reload) {
                        connection.setUseCaches(false);
                    }
                    try (InputStream stream = connection.getInputStream()) {
                        if (stream != null) {
                            BufferedInputStream bis = new BufferedInputStream(stream);
                            bundle = new XMLResourceBundle(bis);
                        }
                    }
                }
            }
        }
        return bundle;
    }

    private static class XMLResourceBundle extends ResourceBundle {

        private Properties props;

        XMLResourceBundle(InputStream stream) throws IOException {
            props = new Properties();
            props.loadFromXML(stream);
        }

        protected Object handleGetObject(String key) {
            if (key == null) {
                throw new NullPointerException();
            }
            return props.get(key);
        }

        public Enumeration<String> getKeys() {
            return null;
        }
    }
}
