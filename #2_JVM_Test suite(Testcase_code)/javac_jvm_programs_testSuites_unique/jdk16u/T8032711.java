



import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.LambdaConversionException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class T8032711 {

    interface I {
        void m();
    }

    static void here() {}
    static MethodHandles.Lookup l;
    static MethodHandle h;
    private static MethodType mt(Class<?> k) { return MethodType.methodType(k); }
    private static boolean mf(Class<?> k) {
        try {
            LambdaMetafactory.metafactory(l, "m",
                mt(I.class),mt(k),h,mt(void.class));
        } catch(LambdaConversionException e) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) throws Throwable {
        l = MethodHandles.lookup();
        h = l.findStatic(T8032711.class, "here", mt(void.class));
        if (mf(void.class)) throw new AssertionError("Error: Should work");
        if (!mf(String.class)) throw new AssertionError("Error: Should fail");
    }
}
