import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;

public class TwiceIndirectlyLoadABundle {

    private static final String rbName = "StackSearchableResource";

    public boolean loadAndTest() throws Throwable {
        String testDir = System.getProperty("test.src", System.getProperty("user.dir"));
        String testClassesDir = System.getProperty("test.classes", System.getProperty("user.dir"));
        URL[] urls = new URL[2];
        urls[0] = Paths.get(testDir, "resources").toUri().toURL();
        urls[1] = Paths.get(testClassesDir).toUri().toURL();
        URLClassLoader yetAnotherResourceCL = new URLClassLoader(urls, null);
        Class<?> loadItUp2InvokerClazz = Class.forName("LoadItUp2Invoker", true, yetAnotherResourceCL);
        ClassLoader actual = loadItUp2InvokerClazz.getClassLoader();
        if (actual != yetAnotherResourceCL) {
            throw new Exception("LoadItUp2Invoker was loaded by an unexpected CL: " + actual);
        }
        Object loadItUp2Invoker = loadItUp2InvokerClazz.newInstance();
        Method setupMethod = loadItUp2InvokerClazz.getMethod("setup", urls.getClass(), String.class);
        try {
            URL[] noResourceUrl = new URL[1];
            noResourceUrl[0] = urls[1];
            setupMethod.invoke(loadItUp2Invoker, noResourceUrl, rbName);
        } catch (InvocationTargetException ex) {
            throw ex.getTargetException();
        }
        Method testMethod = loadItUp2InvokerClazz.getMethod("test");
        try {
            return (Boolean) testMethod.invoke(loadItUp2Invoker);
        } catch (InvocationTargetException ex) {
            throw ex.getTargetException();
        }
    }
}
