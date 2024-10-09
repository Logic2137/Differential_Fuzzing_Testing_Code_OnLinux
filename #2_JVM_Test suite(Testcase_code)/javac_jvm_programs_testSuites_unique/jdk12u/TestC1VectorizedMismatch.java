



import java.util.Arrays;

public class TestC1VectorizedMismatch {

    private static final int NUM_RUNS = 10000;
    private static final int ARRAY_SIZE = 10000;
    private static int[] a;
    private static int[] b;

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
        System.arraycopy(a, 0, b, 0, ARRAY_SIZE);
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
