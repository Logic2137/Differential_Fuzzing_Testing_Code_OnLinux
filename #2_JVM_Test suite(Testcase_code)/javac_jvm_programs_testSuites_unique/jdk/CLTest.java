


package p.q;

import java.io.*;
import java.net.*;
import java.util.*;
import com.sun.tools.classfile.ClassFile;

public class CLTest {
    public static void main(String... args) throws Exception {
        try {
            new CLTest().run();
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    void run() throws Exception {
        String[] names = {
                "p/q/CLTest.class",
                "p/q/CLTest$Inner.class",
                "p/q/CLTest2.class",
                "java/lang/Object.class",
                "UNKNOWN.class",
                "UNKNOWN"
        };

        for (String name : names) {
            testGetResource(name);
            testGetResources(name);
            testGetResourceAsStream(name);
        }

        if (errors > 0) {
            throw new Exception(errors + " errors found");
        }
    }

    void testGetResource(String name) {
        System.err.println("testGetResource: " + name);
        try {
            ClassLoader cl = getClass().getClassLoader();
            URL u = cl.getResource(name);
            if (u == null) {
                if (!name.contains("UNKNOWN")) {
                    error("resource not found: " + name);
                }
                return;
            }

            checkURL(u);
            checkClass(name, u);

        } catch (Throwable t) {
            t.printStackTrace(System.err);
            error("unexpected exception: " + t);
        }
    }

    void testGetResources(String name) {
        System.err.println("testGetResources: " + name);
        try {
            ClassLoader cl = getClass().getClassLoader();
            Enumeration<URL> e = cl.getResources(name);
            List<URL> list = new ArrayList<>();
            while (e.hasMoreElements()) {
                list.add(e.nextElement());
            }

            switch (list.size()) {
                case 0:
                    if (!name.contains("UNKNOWN")) {
                        error("resource not found: " + name);
                    }
                    break;

                case 1:
                    checkClass(name, list.get(0));
                    break;

                default:
                    error("unexpected resources found: " + list);
            }

        } catch (Throwable t) {
            t.printStackTrace(System.err);
            error("unexpected exception: " + t);
        }
    }

    void testGetResourceAsStream(String name) {
        System.err.println("testGetResourceAsStream: " + name);
        try {
            ClassLoader cl = getClass().getClassLoader();
            try (InputStream in = cl.getResourceAsStream(name)) {
                if (in == null) {
                    if (!name.contains("UNKNOWN")) {
                        error("resource not found: " + name);
                    }
                    return;
                }

                checkClass(name, in);
            }
        } catch (Throwable t) {
            t.printStackTrace(System.err);
            error("unexpected exception: " + t);
        }
    }

    void checkClass(String name, URL u) throws Exception {
        try (InputStream in = u.openConnection().getInputStream()) {
            checkClass(name, in);
        }
    }

    void checkClass(String name, InputStream in) throws Exception {
        ClassFile cf = ClassFile.read(in);
        System.err.println("    class " + cf.getName());
        if (!name.equals(cf.getName() + ".class")) {
            error("unexpected class found: " + cf.getName());
        }
    }

    void checkURL(URL url) {
        try {
            
            url.toURI();
        } catch (URISyntaxException e) {
            error("bad URL: " + url + "; " + e);
        }
    }

    void error(String message) {
        System.err.println("Error: " + message);
        errors++;
    }

    int errors = 0;

    class Inner { }
}

class CLTest2 { }
