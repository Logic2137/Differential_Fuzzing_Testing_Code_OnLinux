

import java.lang.reflect.*;
import java.security.*;
import java.util.Arrays;


public class SimpleProxy {
    public static void main(String[] args) throws Exception {
        ClassLoader loader = SimpleProxy.class.getClassLoader();
        Class<?> fooClass = Class.forName("p.Foo");
        Class<?> barClass = Class.forName("p.Bar");

        makeProxy(loader, fooClass);

        System.setSecurityManager(new SecurityManager());
        try {
            makeProxy(loader, barClass);
            throw new RuntimeException("should fail to new proxy instance of a non-public interface");
        } catch (AccessControlException e) {
            if (e.getPermission().getClass() != ReflectPermission.class ||
                    !e.getPermission().getName().equals("newProxyInPackage.p")) {
                throw e;
            }
        }
    }

    private static void makeProxy(ClassLoader loader, Class<?> cls) {
        Class<?>[] intfs = new Class<?>[] { cls };
        Proxy.newProxyInstance(loader, intfs, new InvocationHandler() {
            public Object invoke(Object proxy, Method method, Object[] args)
                    throws Throwable {
                Class<?>[] intfs = proxy.getClass().getInterfaces();
                System.out.println("Proxy for " + Arrays.toString(intfs)
                        + " " + method.getName() + " is being invoked");
                return null;
            }
        });
    }
}
