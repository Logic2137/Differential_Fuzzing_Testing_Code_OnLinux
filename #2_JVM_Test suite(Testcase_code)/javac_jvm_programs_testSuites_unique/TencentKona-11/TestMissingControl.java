



package compiler.arraycopy;

public class TestMissingControl {

    static int[] m1(int[] a2) {
        int[] a1 = new int[10];
        System.arraycopy(a1, 0, a2, 0, 10);
        return a1;
    }

    static class A {
    }

    static Object m2(Object[] a2) {
        A[] a1 = new A[10];
        System.arraycopy(a1, 0, a2, 0, 10);
        return a1;
    }

    static void test1() {
        int[] a2 = new int[10];
        int[] a3 = new int[5];

        
        for (int i = 0; i < 20000; i++) {
            m1(a2);
        }

        
        for (int i = 0; i < 10; i++) {
            try {
                m1(a3);
            } catch(IndexOutOfBoundsException ioobe) {
            }
        }

        
        for (int i = 0; i < 20000; i++) {
            m1(a2);
        }

        try {
            m1(null);
        } catch(NullPointerException npe) {}
    }

    static void test2() {
        A[] a2 = new A[10];
        A[] a3 = new A[5];

        
        for (int i = 0; i < 20000; i++) {
            m2(a2);
        }

        
        for (int i = 0; i < 10; i++) {
            try {
                m2(a3);
            } catch(IndexOutOfBoundsException ioobe) {
            }
        }

        
        for (int i = 0; i < 20000; i++) {
            m2(a2);
        }

        try {
            m2(null);
        } catch(NullPointerException npe) {}
    }

    static public void main(String[] args) {
        test1();
        test2();
    }
}
