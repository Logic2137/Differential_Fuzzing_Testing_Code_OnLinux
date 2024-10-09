
package compiler.profiling;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class TestMethodHandleInvokesIntrinsic {

    static final MethodHandle mh_nanoTime;

    static final MethodHandle mh_getClass;

    static {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodType mt = MethodType.methodType(long.class);
        MethodHandle MH = null;
        try {
            MH = lookup.findStatic(System.class, "nanoTime", mt);
        } catch (NoSuchMethodException nsme) {
            nsme.printStackTrace();
            throw new RuntimeException("TEST FAILED", nsme);
        } catch (IllegalAccessException iae) {
            iae.printStackTrace();
            throw new RuntimeException("TEST FAILED", iae);
        }
        mh_nanoTime = MH;
        mt = MethodType.methodType(Class.class);
        MH = null;
        try {
            MH = lookup.findVirtual(Object.class, "getClass", mt);
        } catch (NoSuchMethodException nsme) {
            nsme.printStackTrace();
            throw new RuntimeException("TEST FAILED", nsme);
        } catch (IllegalAccessException iae) {
            iae.printStackTrace();
            throw new RuntimeException("TEST FAILED", iae);
        }
        mh_getClass = MH;
    }

    static long m1() throws Throwable {
        return (long) mh_nanoTime.invokeExact();
    }

    static Class m2(Object o) throws Throwable {
        return (Class) mh_getClass.invokeExact(o);
    }

    static public void main(String[] args) {
        try {
            for (int i = 0; i < 20000; i++) {
                m1();
            }
            TestMethodHandleInvokesIntrinsic o = new TestMethodHandleInvokesIntrinsic();
            for (int i = 0; i < 20000; i++) {
                m2(o);
            }
        } catch (Throwable t) {
            System.out.println("Unexpected exception");
            t.printStackTrace();
            throw new RuntimeException("TEST FAILED", t);
        }
        System.out.println("TEST PASSED");
    }
}
