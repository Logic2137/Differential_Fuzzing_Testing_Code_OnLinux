
package compiler.c1;

public class CanonicalizeArrayLength {

    int[] arr = new int[42];

    int[] arrNull = null;

    final int[] finalArr = new int[42];

    final int[] finalArrNull = null;

    static int[] staticArr = new int[42];

    static int[] staticArrNull = null;

    static final int[] staticFinalArr = new int[42];

    static final int[] staticFinalArrNull = null;

    public static void main(String... args) {
        CanonicalizeArrayLength t = new CanonicalizeArrayLength();
        for (int i = 0; i < 20000; i++) {
            if (t.testLocal() != 42)
                throw new IllegalStateException();
            if (t.testLocalNull() != 42)
                throw new IllegalStateException();
            if (t.testField() != 42)
                throw new IllegalStateException();
            if (t.testFieldNull() != 42)
                throw new IllegalStateException();
            if (t.testFinalField() != 42)
                throw new IllegalStateException();
            if (t.testFinalFieldNull() != 42)
                throw new IllegalStateException();
            if (t.testStaticField() != 42)
                throw new IllegalStateException();
            if (t.testStaticFieldNull() != 42)
                throw new IllegalStateException();
            if (t.testStaticFinalField() != 42)
                throw new IllegalStateException();
            if (t.testStaticFinalFieldNull() != 42)
                throw new IllegalStateException();
        }
    }

    int testField() {
        try {
            return arr.length;
        } catch (Throwable t) {
            return -1;
        }
    }

    int testFieldNull() {
        try {
            return arrNull.length;
        } catch (Throwable t) {
            return 42;
        }
    }

    int testFinalField() {
        try {
            return finalArr.length;
        } catch (Throwable t) {
            return -1;
        }
    }

    int testFinalFieldNull() {
        try {
            return finalArrNull.length;
        } catch (Throwable t) {
            return 42;
        }
    }

    int testStaticField() {
        try {
            return staticArr.length;
        } catch (Throwable t) {
            return -1;
        }
    }

    int testStaticFieldNull() {
        try {
            return staticArrNull.length;
        } catch (Throwable t) {
            return 42;
        }
    }

    int testStaticFinalField() {
        try {
            return staticFinalArr.length;
        } catch (Throwable t) {
            return -1;
        }
    }

    int testStaticFinalFieldNull() {
        try {
            return staticFinalArrNull.length;
        } catch (Throwable t) {
            return 42;
        }
    }

    int testLocal() {
        int[] arr = new int[42];
        try {
            return arr.length;
        } catch (Throwable t) {
            return -1;
        }
    }

    int testLocalNull() {
        int[] arrNull = null;
        try {
            return arrNull.length;
        } catch (Throwable t) {
            return 42;
        }
    }
}
