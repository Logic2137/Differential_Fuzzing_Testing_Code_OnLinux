import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;

public class TestProfileConflictClassUnloading {

    static class A {
    }

    static void m1(Object o) {
    }

    static void m2(Object o) {
        m1(o);
    }

    static void m3(A a, boolean do_call) {
        if (!do_call) {
            return;
        }
        m2(a);
    }

    public static ClassLoader newClassLoader() {
        try {
            return new URLClassLoader(new URL[] { Paths.get(System.getProperty("test.classes", ".")).toUri().toURL() }, null);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Unexpected URL conversion failure", e);
        }
    }

    public static void main(String[] args) throws Exception {
        ClassLoader loader = newClassLoader();
        Object o = loader.loadClass("B").newInstance();
        for (int i = 0; i < 5000; i++) {
            m2(o);
        }
        A a = new A();
        for (int i = 0; i < 5000; i++) {
            m3(a, false);
        }
        o = null;
        loader = null;
        System.gc();
        m3(a, true);
        System.gc();
    }
}
