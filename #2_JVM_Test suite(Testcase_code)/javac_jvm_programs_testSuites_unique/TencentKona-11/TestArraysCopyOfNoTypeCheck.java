



package compiler.arraycopy;

import java.util.Arrays;

public class TestArraysCopyOfNoTypeCheck {

    static class A {
    }

    static class B extends A {
    }

    static B[] test(A[] arr) {
        return Arrays.copyOf(arr, 10, B[].class);
    }

    static public void main(String[] args) {
        A[] arr = new A[20];
        for (int i = 0; i < 20000; i++) {
            test(arr);
        }
        A[] arr2 = new A[20];
        arr2[0] = new A();
        boolean exception = false;
        try {
            test(arr2);
        } catch (ArrayStoreException ase) {
            exception = true;
        }
        if (!exception) {
            throw new RuntimeException("TEST FAILED: ArrayStoreException not thrown");
        }
    }
}
