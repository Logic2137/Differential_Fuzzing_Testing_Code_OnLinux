import java.lang.invoke.*;
import java.util.*;

public class MetafactoryMethodNameTest {

    public static void main(String... args) {
        goodName("x");
        goodName("xy");
        goodName("]");
        goodName("x]");
        goodName("]y");
        goodName("x]y");
        goodName("&");
        goodName("x&");
        goodName("&y");
        goodName("x&y");
        badName(".");
        badName("x.");
        badName(".y");
        badName("x.y");
        badName(";");
        badName("x;");
        badName(";y");
        badName("x;y");
        badName("[");
        badName("x[");
        badName("[y");
        badName("x[y");
        badName("/");
        badName("x/");
        badName("/y");
        badName("x/y");
        badName("<");
        badName("x<");
        badName("<y");
        badName("x<y");
        badName(">");
        badName("x>");
        badName(">y");
        badName("x>y");
        badName("");
        badName("<init>");
        badName("<clinit>");
    }

    static MethodType mt(Class<?> ret, Class<?>... params) {
        return MethodType.methodType(ret, params);
    }

    static MethodHandle smh(Class<?> c, String name, MethodType desc) {
        try {
            return MethodHandles.lookup().findStatic(c, name, desc);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    static Object[] arr(Object... args) {
        return args;
    }

    public static class C {

        public static void m() {
        }
    }

    public interface I {
    }

    private static MethodHandles.Lookup lookup = MethodHandles.lookup();

    private static MethodType toI = mt(I.class);

    private static MethodType toVoid = mt(void.class);

    private static MethodHandle mh = smh(C.class, "m", toVoid);

    private static Class<?> lce = LambdaConversionException.class;

    static void goodName(String name) {
        succeedMFLinkage(lookup, name, toI, toVoid, mh, toVoid);
        succeedAltMFLinkage(lookup, name, toI, arr(toVoid, mh, toVoid, LambdaMetafactory.FLAG_SERIALIZABLE));
    }

    static void badName(String name) {
        failMFLinkage(lookup, name, toI, toVoid, mh, toVoid, lce);
        failAltMFLinkage(lookup, name, toI, arr(toVoid, mh, toVoid, LambdaMetafactory.FLAG_SERIALIZABLE), lce);
    }

    static CallSite succeedMFLinkage(MethodHandles.Lookup lookup, String name, MethodType capType, MethodType desc, MethodHandle impl, MethodType checked) {
        try {
            return LambdaMetafactory.metafactory(lookup, name, capType, desc, impl, checked);
        } catch (Throwable t) {
            String msg = String.format("Unexpected exception during linkage for metafactory(%s, %s, %s, %s, %s, %s)", lookup, name, capType, desc, impl, checked);
            throw new AssertionError(msg, t);
        }
    }

    static void failMFLinkage(MethodHandles.Lookup lookup, String name, MethodType capType, MethodType desc, MethodHandle impl, MethodType checked, Class<?> expectedExceptionType) {
        try {
            LambdaMetafactory.metafactory(lookup, name, capType, desc, impl, checked);
        } catch (Throwable t) {
            if (expectedExceptionType.isInstance(t)) {
                return;
            } else {
                String msg = String.format("Unexpected exception: expected %s during linkage for metafactory(%s, %s, %s, %s, %s, %s)", expectedExceptionType.getName(), lookup, name, capType, desc, impl, checked);
                throw new AssertionError(msg, t);
            }
        }
        String msg = String.format("Unexpected success: expected %s during linkage for metafactory(%s, %s, %s, %s, %s, %s)", expectedExceptionType.getName(), lookup, name, capType, desc, impl, checked);
        throw new AssertionError(msg);
    }

    static CallSite succeedAltMFLinkage(MethodHandles.Lookup lookup, String name, MethodType capType, Object[] args) {
        try {
            return LambdaMetafactory.altMetafactory(lookup, name, capType, args);
        } catch (Throwable t) {
            String msg = String.format("Unexpected exception during linkage for metafactory(%s, %s, %s, %s)", lookup, name, capType, Arrays.asList(args));
            throw new AssertionError(msg, t);
        }
    }

    static void failAltMFLinkage(MethodHandles.Lookup lookup, String name, MethodType capType, Object[] args, Class<?> expectedExceptionType) {
        try {
            LambdaMetafactory.altMetafactory(lookup, name, capType, args);
        } catch (Throwable t) {
            if (expectedExceptionType.isInstance(t)) {
                return;
            } else {
                String msg = String.format("Unexpected exception: expected %s during linkage for metafactory(%s, %s, %s, %s)", expectedExceptionType.getName(), lookup, name, capType, Arrays.asList(args));
                throw new AssertionError(msg, t);
            }
        }
        String msg = String.format("Unexpected success: expected %s during linkage for metafactory(%s, %s, %s, %s)", expectedExceptionType.getName(), lookup, name, capType, Arrays.asList(args));
        throw new AssertionError(msg);
    }
}
