



package compiler.arraycopy;

import java.util.Arrays;

public class TestArrayCopyOfStopped {
    static class A {
    }

    static class B {
    }

    static final B[] array_of_bs = new B[10];
    static final A[] array_of_as = new A[10];

    static Object[] m1_helper(Object[] array, boolean flag) {
        if (flag) {
            return Arrays.copyOf(array, 10, A[].class);
        }
        return null;
    }

    static Object[] m1(boolean flag) {
        return m1_helper(array_of_bs, flag);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 20000; i++) {
            m1_helper(array_of_as, (i%2) == 0);
        }

        for (int i = 0; i < 20000; i++) {
            m1(false);
        }
    }
}
