



import java.io.Closeable;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.LambdaConversionException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class T8032704 {

    public static void here() {}
    static MethodHandle h;
    private static MethodType mt(Class<?> k) { return MethodType.methodType(k); }
    private static boolean mf(MethodHandles.Lookup l) {
        try {
            LambdaMetafactory.metafactory(l, "close",
                mt(Closeable.class),mt(void.class),h,mt(void.class));
        } catch(LambdaConversionException e) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) throws Throwable {
        MethodHandles.Lookup ll = MethodHandles.lookup();
        h = ll.findStatic(T8032704.class, "here", mt(void.class));
        if (mf(ll)) throw new AssertionError("Error: Should work");
        if (!mf(MethodHandles.publicLookup())) throw new AssertionError("Error: Should fail - public");
        if (!mf(ll.in(T8032704other.class))) throw new AssertionError("Error: Should fail - other");
        if (!mf(ll.in(Thread.class))) throw new AssertionError("Error: Should fail - Thread");
    }
}

class T8032704other {}
