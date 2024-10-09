








package compiler.arraycopy;

public class TestEliminatedArrayCopyDeopt {

    static class A implements Cloneable {
        int f0;
        int f1;
        int f2;
        int f3;
        int f4;
        int f5;
        int f6;
        int f7;
        int f8;
        int f9;
        int f10;
        int f11;
        int f12;
        int f13;
        int f14;
        int f15;

        public Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }

    
    static boolean m1(A a, boolean flag) throws CloneNotSupportedException {
        A c = (A)a.clone();
        if (flag) {
            
            if (c.f0 != 0x42) {
                return false;
            }
        }
        return true;
    }

    
    static int[] m2_src = null;
    static boolean m2(boolean flag) throws CloneNotSupportedException {
        int[] src  = new int[10];
        m2_src = src;
        for (int i = 0; i < src.length; i++) {
            src[i] = 0x42+i;
        }
        int[] c = (int[])src.clone();
        if (flag) {
            for (int i = 0; i < c.length; i++) {
                if (c[i] != src[i]) {
                    return false;
                }
            }
        }
        return true;
    }

    
    static boolean m3(int[] src, boolean flag) {
        int[] dst = new int[10];
        System.arraycopy(src, 0, dst, 0, 10);
        if (flag) {
            for (int i = 0; i < dst.length; i++) {
                if (dst[i] != src[i]) {
                    return false;
                }
            }
        }
        return true;
    }

    
    static boolean m4(int[] src, boolean flag) {
        int[] dst = new int[10];
        dst[0] = 0x42;
        dst[1] = 0x42 - 1;
        dst[2] = 0x42 - 2;
        dst[8] = 0x42 - 8;
        dst[9] = 0x42 - 9;
        int src_off = 2;
        int dst_off = 3;
        int len = 5;
        System.arraycopy(src, src_off, dst, dst_off, len);
        if (flag) {
            for (int i = 0; i < dst.length; i++) {
                if (i >= dst_off &&  i < dst_off + len) {
                    if (dst[i] != src[i - dst_off + src_off]) {
                        return false;
                    }
                } else {
                    if (dst[i] != 0x42-i) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    
    static boolean m5(int[] src, boolean flag1, boolean flag2) {
        int[] dst = new int[10];
        if (flag1) {
            System.arraycopy(src, 0, dst, 0, 10);
        }
        if (flag2) {
            for (int i = 0; i < dst.length; i++) {
                if (dst[i] != src[i]) {
                    return false;
                }
            }
        }
        return true;
    }

    static public void main(String[] args) throws Exception {
        boolean success = true;
        A a = new A();
        a.f0 = 0x42;
        for (int i = 0; i < 20000; i++) {
            m1(a, false);
        }
        if (!m1(a, true)) {
            System.out.println("m1 failed");
            success = false;
        }

        for (int i = 0; i < 20000; i++) {
            m2(false);
        }
        if (!m2(true)) {
            System.out.println("m2 failed");
            success = false;
        }

        int[] src = new int[10];
        for (int i = 0; i < src.length; i++) {
            src[i] = 0x42+i;
        }

        for (int i = 0; i < 20000; i++) {
            m3(src, false);
        }
        if (!m3(src, true)) {
            System.out.println("m3 failed");
            success = false;
        }

        for (int i = 0; i < 20000; i++) {
            m4(src, false);
        }
        if (!m4(src, true)) {
            System.out.println("m4 failed");
            success = false;
        }

        for (int i = 0; i < 20000; i++) {
            m5(src, i%2 == 0, false);
        }
        if (!m5(src, true, true)) {
            System.out.println("m4 failed");
            success = false;
        }

        if (!success) {
            throw new RuntimeException("Test failed");
        }
    }
}
