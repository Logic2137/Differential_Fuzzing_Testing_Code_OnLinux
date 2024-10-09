



package compiler.linkage;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

interface I {
    void m1(int i);
    static void s1() {}
}

class A implements I {
    public void m1(int i) {}
}

class X {
    public        void m1(int i) {}
    public final  void f1(int i) {}
    public static void s1(int i) {}
}

public class LinkageErrors {
    final static MethodHandles.Lookup L = MethodHandles.lookup();

    static void testICCE(MethodHandle mh) {
        try {
            mh.invokeExact();
            throw new AssertionError("No exception thrown");
        } catch (IncompatibleClassChangeError e) {
            return; 
        } catch (AssertionError e) {
            throw e; 
        } catch (Throwable e) {
            throw new AssertionError("Unexpected exception", e);
        }
    }

    static void testNSME(MethodHandle mh) {
        try {
            mh.invokeExact();
            throw new AssertionError("No exception thrown");
        } catch (NoSuchMethodError e) {
            return; 
        } catch (AssertionError e) {
            throw e; 
        } catch (Throwable e) {
            throw new AssertionError("Unexpected exception", e);
        }
    }

    public static void main(String args[]) throws Throwable {
        Class<?> test = Class.forName("compiler.linkage.CallSites");

        
        MethodHandle testI1 = L.findStatic(test, "testI1", MethodType.methodType(void.class, I.class));
        MethodHandle testX1 = L.findStatic(test, "testX1", MethodType.methodType(void.class, X.class));

        MethodHandle testI1_A    = testI1.bindTo(new A());
        MethodHandle testI1_null = testI1.bindTo(null);
        MethodHandle testX1_X    = testX1.bindTo(new X());
        MethodHandle testX1_null = testX1.bindTo(null);

        
        MethodHandle testI2 = L.findStatic(test, "testI2", MethodType.methodType(void.class));
        MethodHandle testX2 = L.findStatic(test, "testX2", MethodType.methodType(void.class));

        MethodHandle testI3 = L.findStatic(test, "testI3", MethodType.methodType(void.class, I.class));
        MethodHandle testX3 = L.findStatic(test, "testX3", MethodType.methodType(void.class, X.class));

        
        MethodHandle testI3_A    = testI3.bindTo(new A());
        MethodHandle testI3_null = testI3.bindTo(null);
        MethodHandle testX3_X    = testX3.bindTo(new X());
        MethodHandle testX3_null = testX3.bindTo(null);

        for (int i = 0; i < 20_000; i++) {
            testNSME(testI1_A);
            testNSME(testI1_null);
            testNSME(testX1_X);
            testNSME(testX1_null);

            testICCE(testI2);
            testICCE(testX2);

            testICCE(testI3_A);
            testICCE(testI3_null);
            testICCE(testX3_X);
            testICCE(testX3_null);
        }

        System.out.println("TEST PASSED");
    }
}
