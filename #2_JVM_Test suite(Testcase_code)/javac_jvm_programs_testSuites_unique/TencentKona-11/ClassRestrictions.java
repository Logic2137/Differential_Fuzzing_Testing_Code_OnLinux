



import java.io.File;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.net.URLClassLoader;
import java.net.URL;
import java.nio.file.Paths;

public class ClassRestrictions {

    public interface Bar {
        int foo();
    }

    public interface Baz {
        long foo();
    }

    interface Bashful {
        void foo();
    }

    public static final String nonPublicIntrfaceName = "java.util.zip.ZipConstants";

    public static void main(String[] args) {

        System.err.println(
            "\nTest of restrictions on parameters to Proxy.getProxyClass\n");

        try {
            ClassLoader loader = ClassRestrictions.class.getClassLoader();
            Class<?>[] interfaces;
            Class<?> proxyClass;

            
            try {
                interfaces = new Class<?>[] { Object.class };
                proxyClass = Proxy.getProxyClass(loader, interfaces);
                throw new Error(
                    "proxy class created with java.lang.Object as interface");
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                System.err.println();
                
            }
            try {
                interfaces = new Class<?>[] { Integer.TYPE };
                proxyClass = Proxy.getProxyClass(loader, interfaces);
                throw new Error(
                    "proxy class created with int.class as interface");
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                System.err.println();
                
            }

            
            try {
                interfaces = new Class<?>[] { Bar.class, Bar.class };
                proxyClass = Proxy.getProxyClass(loader, interfaces);
                throw new Error(
                    "proxy class created with repeated interfaces");
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                System.err.println();
                
            }

            
            String[] cpaths = System.getProperty("test.classes", ".")
                                    .split(File.pathSeparator);
            URL[] urls = new URL[cpaths.length];
            for (int i=0; i < cpaths.length; i++) {
                urls[i] = Paths.get(cpaths[i]).toUri().toURL();
            }
            ClassLoader altLoader = new URLClassLoader(urls, null);
            Class altBarClass;
            altBarClass = Class.forName(Bar.class.getName(), false, altLoader);
            try {
                interfaces = new Class<?>[] { altBarClass };
                proxyClass = Proxy.getProxyClass(loader, interfaces);
                throw new Error(
                    "proxy class created with interface " +
                    "not visible to class loader");
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                System.err.println();
                
            }

            
            Class<?> nonPublic1 = Bashful.class;
            Class<?> nonPublic2 = Class.forName(nonPublicIntrfaceName);
            if (Modifier.isPublic(nonPublic2.getModifiers())) {
                throw new Error(
                    "Interface " + nonPublicIntrfaceName +
                    " is public and need to be changed!");
            }
            try {
                interfaces = new Class<?>[] { nonPublic1, nonPublic2 };
                proxyClass = Proxy.getProxyClass(loader, interfaces);
                throw new Error(
                    "proxy class created with two non-public interfaces " +
                    "in different packages");
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                System.err.println();
                
            }

            
            try {
                interfaces = new Class<?>[] { Bar.class, Baz.class };
                proxyClass = Proxy.getProxyClass(loader, interfaces);
                throw new Error(
                    "proxy class created with conflicting methods");
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                System.err.println();
                
            }

            
            System.err.println("\nTEST PASSED");

        } catch (Throwable e) {
            System.err.println("\nTEST FAILED:");
            e.printStackTrace();
            throw new Error("TEST FAILED: ", e);
        }
    }
}
