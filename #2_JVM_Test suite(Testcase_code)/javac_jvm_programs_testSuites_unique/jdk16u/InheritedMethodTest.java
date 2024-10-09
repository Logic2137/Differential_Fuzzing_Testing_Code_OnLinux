


import java.lang.ReflectiveOperationException;
import java.lang.invoke.*;

public class InheritedMethodTest {

    public static MethodType mt(Class<?> ret, Class<?>... params) { return MethodType.methodType(ret, params); }

    public interface StringFactory {
        String get();
    }

    public interface I {
        String iString();
    }

    public interface J extends I {}

    public static abstract class C implements I {}

    public static class D extends C implements J {
        public String toString() { return "a"; }
        public String iString() { return "b"; }
    }

    private static final MethodHandles.Lookup lookup = MethodHandles.lookup();

    public static void main(String... args) throws Throwable {
        test(lookup.findVirtual(C.class, "toString", mt(String.class)), "a");
        test(lookup.findVirtual(C.class, "iString", mt(String.class)), "b");
        test(lookup.findVirtual(J.class, "toString", mt(String.class)), "a");
        test(lookup.findVirtual(J.class, "iString", mt(String.class)), "b");
        test(lookup.findVirtual(I.class, "toString", mt(String.class)), "a");
        test(lookup.findVirtual(I.class, "iString", mt(String.class)), "b");
    }

    static void test(MethodHandle implMethod, String expected) throws Throwable {
        testMetafactory(implMethod, expected);
        testAltMetafactory(implMethod, expected);
    }

    static void testMetafactory(MethodHandle implMethod, String expected) throws Throwable {
        CallSite cs = LambdaMetafactory.metafactory(lookup, "get", mt(StringFactory.class, D.class), mt(String.class),
                                                    implMethod, mt(String.class));
        StringFactory factory = (StringFactory) cs.dynamicInvoker().invokeExact(new D());
        String actual = factory.get();
        if (!expected.equals(actual)) throw new AssertionError("Unexpected result: " + actual);
    }

    static void testAltMetafactory(MethodHandle implMethod, String expected) throws Throwable {
        CallSite cs = LambdaMetafactory.altMetafactory(lookup, "get", mt(StringFactory.class, D.class), mt(String.class),
                                                       implMethod, mt(String.class), LambdaMetafactory.FLAG_SERIALIZABLE);
        StringFactory factory = (StringFactory) cs.dynamicInvoker().invokeExact(new D());
        String actual = factory.get();
        if (!expected.equals(actual)) throw new AssertionError("Unexpected result: " + actual);
    }

}
