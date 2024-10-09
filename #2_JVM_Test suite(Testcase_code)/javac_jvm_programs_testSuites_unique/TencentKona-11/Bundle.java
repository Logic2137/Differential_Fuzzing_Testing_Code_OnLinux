
package org.netbeans.jemmy;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Properties;
import java.util.jar.JarFile;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;


public class Bundle extends Object {

    private Properties resources;

    
    public Bundle() {
        resources = new Properties();
    }

    
    public void load(InputStream stream)
            throws IOException {
        resources.load(stream);
    }

    
    public void loadFromFile(String fileName)
            throws IOException, FileNotFoundException {
        try (FileInputStream fileInputStream = new FileInputStream(fileName)) {
            load(fileInputStream);
        }
    }

    
    public void loadFromJar(String fileName, String entryName)
            throws IOException, FileNotFoundException {
        try (JarFile jFile = new JarFile(fileName);
                InputStream inputStream = jFile.getInputStream(jFile.getEntry(entryName))) {
            load(inputStream);
        }
    }

    
    public void loadFromZip(String fileName, String entryName)
            throws IOException, FileNotFoundException, ZipException {
        try (ZipFile zFile = new ZipFile(fileName);
                InputStream inputStream = zFile.getInputStream(zFile.getEntry(entryName))) {
            load(inputStream);
        }
    }

    
    public void print(PrintWriter writer) {
        Enumeration<Object> keys = resources.keys();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            writer.println(key + "=" + getResource(key));
        }
    }

    
    public void print(PrintStream stream) {
        print(new PrintWriter(stream));
    }

    
    public String getResource(String key) {
        return resources.getProperty(key);
    }

}
