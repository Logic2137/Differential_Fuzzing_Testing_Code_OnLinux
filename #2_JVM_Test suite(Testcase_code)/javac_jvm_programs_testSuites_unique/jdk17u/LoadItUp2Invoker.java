import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class LoadItUp2Invoker {

    private URLClassLoader cl;

    private String rbName;

    private Object loadItUp2;

    private Method testMethod;

    public void setup(URL[] urls, String rbName) throws ReflectiveOperationException {
        this.cl = new URLClassLoader(urls, null);
        this.rbName = rbName;
        Class<?> loadItUp2Clazz = Class.forName("LoadItUp2", true, cl);
        this.loadItUp2 = loadItUp2Clazz.newInstance();
        this.testMethod = loadItUp2Clazz.getMethod("test", String.class);
    }

    public Boolean test() throws Throwable {
        try {
            return (Boolean) testMethod.invoke(loadItUp2, rbName);
        } catch (InvocationTargetException ex) {
            throw ex.getTargetException();
        }
    }
}
