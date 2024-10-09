
package compiler.tiered;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.Callable;

public class MethodHelper {

    public static Method getMethod(Class<?> aClass, String name) {
        Method method;
        try {
            method = aClass.getDeclaredMethod(name);
        } catch (NoSuchMethodException e) {
            throw new Error("TESTBUG: Unable to get method " + name, e);
        }
        return method;
    }

    public static Callable<Integer> getCallable(Object object, String name) {
        Method method = getMethod(object.getClass(), name);
        return () -> {
            try {
                return Objects.hashCode(method.invoke(object));
            } catch (ReflectiveOperationException e) {
                throw new Error("TESTBUG: Invocation failure", e);
            }
        };
    }
}
