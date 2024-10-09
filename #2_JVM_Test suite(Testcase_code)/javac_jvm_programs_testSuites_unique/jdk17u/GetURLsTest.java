import java.net.*;
import java.io.*;

public class GetURLsTest {

    static final String TEST_DIR = System.getProperty("test.src", ".");

    public static void main(String[] args) throws Exception {
        File testJars = new File(TEST_DIR, "jars");
        MyURLClassLoader ucl = new MyURLClassLoader(new URL[] { new File(".").toURL() });
        p("initial urls = ", ucl.getURLs());
        URL u = ucl.getResource("GetURLsTest.java");
        if (u != null) {
            p("found resource = " + u);
        }
        ucl.addURL(new File(testJars, "class_path_test.jar").toURL());
        p("new urls = ", ucl.getURLs());
        Class c = ucl.loadClass("Foo");
        p("found class = " + c);
    }

    static class MyURLClassLoader extends URLClassLoader {

        public MyURLClassLoader(URL[] urls) {
            super(urls);
        }

        public void addURL(URL url) {
            super.addURL(url);
        }
    }

    static void p(String s, URL[] urls) {
        System.out.print(s);
        if (urls.length > 0) {
            for (int i = 0; i < urls.length - 1; i++) {
                System.out.print(urls[i] + " ");
            }
        }
        System.out.println(urls[urls.length - 1]);
    }

    static void p(String s) {
        System.out.println(s);
    }
}
