



import java.util.Arrays;
import java.util.Random;

public class TestJNIGlobalRefs {
    static {
        System.loadLibrary("TestJNIGlobalRefs");
    }

    private static final int TIME_MSEC = 120000;
    private static final int ARRAY_SIZE = 10000;

    private static native void makeGlobalRef(Object o);
    private static native void makeWeakGlobalRef(Object o);
    private static native Object readGlobalRef();
    private static native Object readWeakGlobalRef();

    public static void main(String[] args) throws Throwable {
        seedGlobalRef();
        seedWeakGlobalRef();
        long start = System.currentTimeMillis();
        long current = start;
        while (current - start < TIME_MSEC) {
            testGlobal();
            testWeakGlobal();
            Thread.sleep(1);
            current = System.currentTimeMillis();
        }
    }

    private static void seedGlobalRef() {
        int[] a = new int[ARRAY_SIZE];
        fillArray(a, 1337);
        makeGlobalRef(a);
    }

    private static void seedWeakGlobalRef() {
        int[] a = new int[ARRAY_SIZE];
        fillArray(a, 8080);
        makeWeakGlobalRef(a);
    }

    private static void testGlobal() {
        int[] a = (int[]) readGlobalRef();
        checkArray(a, 1337);
    }

    private static void testWeakGlobal() {
        int[] a = (int[]) readWeakGlobalRef();
        if (a != null) {
            checkArray(a, 8080);
        } else {
            
            seedWeakGlobalRef();
        }
    }

    private static void fillArray(int[] array, int seed) {
        Random r = new Random(seed);
        for (int i = 0; i < ARRAY_SIZE; i++) {
            array[i] = r.nextInt();
        }
    }

    private static void checkArray(int[] array, int seed) {
        Random r = new Random(seed);
        if (array.length != ARRAY_SIZE) {
            throw new IllegalStateException("Illegal array length: " + array.length + ", but expected " + ARRAY_SIZE);
        }
        for (int i = 0; i < ARRAY_SIZE; i++) {
            int actual = array[i];
            int expected = r.nextInt();
            if (actual != expected) {
                throw new IllegalStateException("Incorrect array data: " + actual + ", but expected " + expected);
            }
        }
    }
}
