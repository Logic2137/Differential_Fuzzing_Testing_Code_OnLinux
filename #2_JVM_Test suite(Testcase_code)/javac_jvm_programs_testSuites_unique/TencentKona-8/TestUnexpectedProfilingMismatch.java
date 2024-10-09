



import java.lang.invoke.*;

public class TestUnexpectedProfilingMismatch {

    static class A {
    }

    static class B {
    }

    static void mA(A a) {
    }

    static void mB(B b) {
    }

    static final MethodHandle mhA;
    static final MethodHandle mhB;
    static {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodType mt = MethodType.methodType(void.class, A.class);
        MethodHandle res = null;
        try {
            res = lookup.findStatic(TestUnexpectedProfilingMismatch.class, "mA", mt);
        } catch(NoSuchMethodException ex) {
        } catch(IllegalAccessException ex) {
        }
        mhA = res;
        mt = MethodType.methodType(void.class, B.class);
        try {
            res = lookup.findStatic(TestUnexpectedProfilingMismatch.class, "mB", mt);
        } catch(NoSuchMethodException ex) {
        } catch(IllegalAccessException ex) {
        }
        mhB = res;
    }

    void m1(A a, boolean doit) throws Throwable {
        if (doit) {
            mhA.invoke(a);
        }
    }

    void m2(B b) throws Throwable {
        mhB.invoke(b);
    }

    static public void main(String[] args) {
        TestUnexpectedProfilingMismatch tih = new TestUnexpectedProfilingMismatch();
        A a = new A();
        B b = new B();
        try {
            for (int i = 0; i < 256 - 1; i++) {
                tih.m1(a, true);
            }
            
            
            
            tih.m1(a, false);
            
            for (int i = 0; i < 256; i++) {
                tih.m2(b);
            }
            
            tih.m1(a, true);
        } catch(Throwable ex) {
            ex.printStackTrace();
        }
        System.out.println("TEST PASSED");
    }
}
