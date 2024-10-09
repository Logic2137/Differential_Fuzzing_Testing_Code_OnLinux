import java.util.Arrays;

public class TestJNICritical {

    static {
        System.loadLibrary("TestJNICritical");
    }

    private static final int NUM_RUNS = 10000;

    private static final int ARRAY_SIZE = 10000;

    private static int[] a;

    private static int[] b;

    private static native void copyAtoB(int[] a, int[] b);

    public static void main(String[] args) {
        a = new int[ARRAY_SIZE];
        b = new int[ARRAY_SIZE];
        for (int i = 0; i < NUM_RUNS; i++) {
            test();
        }
    }

    private static void test() {
        int[] a1 = new int[ARRAY_SIZE];
        int[] b1 = new int[ARRAY_SIZE];
        fillArray(a);
        copyAtoB(a, b);
        copyAtoB(a1, b1);
        if (!Arrays.equals(a, b)) {
            throw new RuntimeException("arrays not equal");
        }
    }

    private static void fillArray(int[] array) {
        for (int i = 0; i < ARRAY_SIZE; i++) {
            int val = (int) (Math.random() * Integer.MAX_VALUE);
            array[i] = val;
        }
    }
}
