





















import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.logging.Logger;

public class CustomLoaderApp {
    public static void ping() {};

    private static void log(String msg) {
        System.out.println("CustomLoaderApp: " + msg);
    }

    public static void main(String[] args) throws Exception {
        String path = args[0];
        URL url = new File(path).toURI().toURL();
        URL[] urls = new URL[] {url};

        String loaderType = args[1];
        log("loaderType = " + loaderType);


        for (int i = 2; i < args.length; i++) {
            String testClass = args[i];
            log("testClass = " + testClass);

            switch(loaderType) {
            case "unregistered":
                loadAndUseWithUnregisteredLoader(urls, testClass);
                break;
            default:
                throw new IllegalArgumentException("loader type is wrong: " + loaderType);
            }
        }
    }


    
    
    private static void loadAndUseWithUnregisteredLoader(URL[] urls, String testClass)
        throws Exception {
        URLClassLoader urlClassLoader = new URLClassLoader(urls);
        callTestMethod(loadAndCheck(urlClassLoader, testClass));
    }

    private static Class loadAndCheck(ClassLoader loader, String className)
        throws ClassNotFoundException {
        Class c = loader.loadClass(className);
        log("class  = " + c);
        log("loader = " + c.getClassLoader());

        
        if (c.getClassLoader() != loader) {
            String msg = String.format("c.getClassLoader() equals to <%s>, expected <%s>",
                                       c.getClassLoader(), loader);
            throw new RuntimeException(msg);
        }
        return c;
    }

    private static void callTestMethod(Class c) throws Exception {
        Method[] methods = c.getDeclaredMethods();
        for (Method m : methods) {
            log("method = " + m.getName());
            if (m.getName().equals("test"))
                m.invoke(null);
        }
    }
}
