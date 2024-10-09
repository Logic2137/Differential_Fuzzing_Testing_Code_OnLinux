
package compiler.profiling;

import java.lang.reflect.Method;

public class TestSpecTrapClassUnloading {

    static class B {

        final public boolean m(Object o) {
            if (o.getClass() == B.class) {
                return true;
            }
            return false;
        }
    }

    static class MemoryChunk {

        MemoryChunk other;

        long[] array;

        MemoryChunk(MemoryChunk other) {
            this.other = other;
            array = new long[1024 * 1024 * 1024];
        }
    }

    static void m1(B b, Object o) {
        b.m(o);
    }

    static void m2(B b, Object o) {
        b.m(o);
    }

    public static void main(String[] args) throws Exception {
        Method m = B.class.getMethod("m", Object.class);
        Object o = new Object();
        B b = new B();
        for (int i = 0; i < 20000; i++) {
            m1(b, b);
        }
        m1(b, o);
        for (int i = 0; i < 20000; i++) {
            m.invoke(b, b);
        }
        m.invoke(b, o);
        m = null;
        for (int i = 0; i < 20000; i++) {
            m2(b, b);
        }
        m2(b, o);
        MemoryChunk root = null;
        try {
            while (true) {
                root = new MemoryChunk(root);
            }
        } catch (OutOfMemoryError e) {
            root = null;
        }
    }
}
