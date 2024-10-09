



import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Observer;

public class ProxyClashTest {

    public interface ClashWithRunnable {
        static int run() { return 123; }

        static void foo() {}
    }

    public static void main(String[] args) {
        System.err.println(
            "\nDynamic proxy API static method clash test\n");

        Class<?>[] interfaces =
            new Class<?>[] { ClashWithRunnable.class, Runnable.class, Observer.class };

        ClassLoader loader = ProxyClashTest.class.getClassLoader();

        
        Class<?> proxyClass = Proxy.getProxyClass(loader, interfaces);
        System.err.println("+ generated proxy class: " + proxyClass);

        for (Method method : proxyClass.getDeclaredMethods()) {
            if (method.getName().equals("run") && method.getReturnType() == int.class) {
                throw new RuntimeException("proxy intercept a static method");
            }
            if (method.getName().equals("foo")) {
                throw new RuntimeException("proxy intercept a static method");
            }
        }

        System.err.println("\nTEST PASSED");
    }
}
