



public class TestC1ArrayCopyNPE {

    private static final int NUM_RUNS = 10000;
    private static final int ARRAY_SIZE = 10000;
    private static int[] a;
    private static int[] b;

    public static void main(String[] args) {
        a = null;
        b = new int[ARRAY_SIZE];
        for (int i = 0; i < NUM_RUNS; i++) {
            test();
        }
        a = new int[ARRAY_SIZE];
        b = null;
        for (int i = 0; i < NUM_RUNS; i++) {
            test();
        }
    }

    private static void test() {
        try {
            System.arraycopy(a, 0, b, 0, ARRAY_SIZE);
            throw new RuntimeException("test failed");
        } catch (NullPointerException ex) {
            
        }
    }
}
