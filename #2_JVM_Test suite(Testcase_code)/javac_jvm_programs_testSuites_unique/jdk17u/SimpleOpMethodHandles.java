
package vm.mlvm.meth.share;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class SimpleOpMethodHandles {

    public static boolean eq(Object o1, Object o2) {
        return o1.equals(o2);
    }

    public static boolean not(boolean a) {
        return !a;
    }

    public static MethodHandle notMH() throws NoSuchMethodException, IllegalAccessException {
        return MethodHandles.lookup().findStatic(SimpleOpMethodHandles.class, "not", MethodType.methodType(boolean.class, boolean.class));
    }

    public static MethodHandle eqMH() throws NoSuchMethodException, IllegalAccessException {
        return MethodHandles.lookup().findStatic(SimpleOpMethodHandles.class, "eq", MethodType.methodType(boolean.class, Object.class, Object.class));
    }
}
