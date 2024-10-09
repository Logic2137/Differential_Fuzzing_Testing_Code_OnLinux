import java.util.jar.*;
import java.util.*;
import java.io.*;
import java.net.*;

public class ClassPathTest {

    JarFile jarFile;

    Manifest manifest;

    Attributes mainAttributes;

    Map map;

    URLClassLoader ucl;

    static class TestException extends RuntimeException {

        TestException(Throwable t) {
            super("URLClassLoader ClassPathTest failed with: " + t);
        }
    }

    public ClassPathTest() {
        File local = new File(System.getProperty("test.src", "."), "jars/class_path_test.jar");
        String jarFileName = local.getPath();
        try {
            jarFile = new JarFile(jarFileName);
        } catch (IOException e) {
            System.err.println("Could not find jar file " + jarFileName);
            throw new TestException(e);
        }
        try {
            URL url = getUrl(new File(jarFileName));
            System.out.println("url: " + url);
            ucl = new URLClassLoader(new URL[] { url });
            manifest = jarFile.getManifest();
        } catch (Exception e) {
            throw new TestException(e);
        }
        mainAttributes = manifest.getMainAttributes();
        map = manifest.getEntries();
        Iterator it = map.entrySet().iterator();
        Class clazz = null;
        while (it.hasNext()) {
            Map.Entry e = (Map.Entry) it.next();
            Attributes a = (Attributes) e.getValue();
            Attributes.Name an = new Attributes.Name("Class-Path");
            if (a.containsKey(an)) {
                String val = a.getValue(an);
                if (val != null)
                    System.out.println("Class-Path: " + val);
            }
            if (a.containsKey(new Attributes.Name("Java-Bean"))) {
                String beanClassName = nameToClassName((String) e.getKey());
                System.out.println("JavaBean Class: " + beanClassName);
                try {
                    clazz = ucl.loadClass(beanClassName);
                } catch (Throwable t) {
                    throw new TestException(t);
                }
                if (clazz != null) {
                    try {
                        System.out.println("instantiating " + beanClassName);
                        clazz.newInstance();
                        System.out.println("done instantiating " + beanClassName);
                    } catch (Throwable t2) {
                        throw new TestException(t2);
                    }
                }
            }
        }
    }

    String nameToClassName(String key) {
        String key2 = key.replace('/', File.separatorChar);
        int li = key2.lastIndexOf(".class");
        key2 = key2.substring(0, li);
        return key2;
    }

    private static URL getUrl(File file) {
        String name;
        try {
            name = file.getCanonicalPath();
        } catch (IOException e) {
            name = file.getAbsolutePath();
        }
        name = name.replace(File.separatorChar, '/');
        if (!name.startsWith("/")) {
            name = "/" + name;
        }
        try {
            return new URL("file:" + name);
        } catch (MalformedURLException e) {
            throw new TestException(e);
        }
    }

    public static void main(String[] args) {
        new ClassPathTest();
    }
}
