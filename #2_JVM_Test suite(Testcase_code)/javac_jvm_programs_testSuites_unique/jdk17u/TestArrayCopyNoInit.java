
package compiler.arraycopy;

public class TestArrayCopyNoInit {

    static int[] m1(int[] src) {
        int[] dest = new int[10];
        try {
            System.arraycopy(src, 0, dest, 0, 10);
        } catch (NullPointerException npe) {
        }
        return dest;
    }

    static int[] m2(Object src, boolean flag) {
        Class tmp = src.getClass();
        if (flag) {
            return null;
        }
        int[] dest = new int[10];
        try {
            System.arraycopy(src, 0, dest, 0, 10);
        } catch (ArrayStoreException npe) {
        }
        return dest;
    }

    static int[] m3(int[] src, int src_offset) {
        int tmp = src[0];
        int[] dest = new int[10];
        try {
            System.arraycopy(src, src_offset, dest, 0, 10);
        } catch (IndexOutOfBoundsException npe) {
        }
        return dest;
    }

    static int[] m4(int[] src, int length) {
        int tmp = src[0];
        int[] dest = new int[10];
        try {
            System.arraycopy(src, 0, dest, 0, length);
        } catch (IndexOutOfBoundsException npe) {
        }
        return dest;
    }

    static TestArrayCopyNoInit[] m5(Object[] src) {
        Object tmp = src[0];
        TestArrayCopyNoInit[] dest = new TestArrayCopyNoInit[10];
        System.arraycopy(src, 0, dest, 0, 10);
        return dest;
    }

    static class A {
    }

    static class B extends A {
    }

    static class C extends B {
    }

    static class D extends C {
    }

    static class E extends D {
    }

    static class F extends E {
    }

    static class G extends F {
    }

    static class H extends G {
    }

    static class I extends H {
    }

    static H[] m6(Object[] src) {
        Object tmp = src[0];
        H[] dest = new H[10];
        System.arraycopy(src, 0, dest, 0, 10);
        return dest;
    }

    static Object m7_src(Object src) {
        return src;
    }

    static int[] m7(Object src, boolean flag) {
        Class tmp = src.getClass();
        if (flag) {
            return null;
        }
        src = m7_src(src);
        int[] dest = new int[10];
        try {
            System.arraycopy(src, 0, dest, 0, 10);
        } catch (ArrayStoreException npe) {
        }
        return dest;
    }

    static public void main(String[] args) {
        boolean success = true;
        int[] src = new int[10];
        TestArrayCopyNoInit[] src2 = new TestArrayCopyNoInit[10];
        int[] res = null;
        TestArrayCopyNoInit[] res2 = null;
        Object src_obj = new Object();
        for (int i = 0; i < 20000; i++) {
            m1(src);
        }
        res = m1(null);
        for (int i = 0; i < res.length; i++) {
            if (res[i] != 0) {
                success = false;
                System.out.println("Uninitialized array following NPE");
                break;
            }
        }
        for (int i = 0; i < 20000; i++) {
            if ((i % 2) == 0) {
                m2(src, false);
            } else {
                m2(src_obj, true);
            }
        }
        res = m2(src_obj, false);
        for (int i = 0; i < res.length; i++) {
            if (res[i] != 0) {
                success = false;
                System.out.println("Uninitialized array following failed array check");
                break;
            }
        }
        for (int i = 0; i < 20000; i++) {
            m3(src, 0);
        }
        res = m3(src, -1);
        for (int i = 0; i < res.length; i++) {
            if (res[i] != 0) {
                success = false;
                System.out.println("Uninitialized array following failed src offset check");
                break;
            }
        }
        for (int i = 0; i < 20000; i++) {
            m4(src, 0);
        }
        res = m4(src, -1);
        for (int i = 0; i < res.length; i++) {
            if (res[i] != 0) {
                success = false;
                System.out.println("Uninitialized array following failed length check");
                break;
            }
        }
        for (int i = 0; i < 20000; i++) {
            m5(src2);
        }
        res2 = m5(new Object[10]);
        for (int i = 0; i < res2.length; i++) {
            if (res2[i] != null) {
                success = false;
                System.out.println("Uninitialized array following failed type check");
                break;
            }
        }
        H[] src3 = new H[10];
        I b = new I();
        for (int i = 0; i < 20000; i++) {
            m6(src3);
        }
        H[] res3 = m6(new Object[10]);
        for (int i = 0; i < res3.length; i++) {
            if (res3[i] != null) {
                success = false;
                System.out.println("Uninitialized array following failed full type check");
                break;
            }
        }
        for (int i = 0; i < 20000; i++) {
            if ((i % 2) == 0) {
                m7(src, false);
            } else {
                m7(src_obj, true);
            }
        }
        res = m7(src_obj, false);
        for (int i = 0; i < res.length; i++) {
            if (res[i] != 0) {
                success = false;
                System.out.println("Uninitialized array following failed type check with return value profiling");
                break;
            }
        }
        if (!success) {
            throw new RuntimeException("Some tests failed");
        }
    }
}
