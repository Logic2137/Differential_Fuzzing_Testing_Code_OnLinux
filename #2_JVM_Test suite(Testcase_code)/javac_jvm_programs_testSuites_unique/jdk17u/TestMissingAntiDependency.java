public class TestMissingAntiDependency {

    public static void main(String[] args) {
        int[] src = new int[10];
        for (int i = 0; i < 20_000; i++) {
            test(src, true, true, 10);
            test(src, true, false, 10);
            test(src, false, false, 10);
        }
        src[9] = 42;
        final int v = test(src, true, true, 1);
        if (v != 42) {
            throw new RuntimeException("Incorrect return value " + v);
        }
    }

    private static int test(int[] src, boolean flag1, boolean flag2, int stop) {
        int v = 0;
        int j = 1;
        for (; j < 10; j++) {
            for (int i = 0; i < 2; i++) {
            }
        }
        int[] dst = new int[10];
        for (int i = 0; i < stop; i++) {
            if (flag1) {
                System.arraycopy(src, 0, dst, 0, j);
                v = dst[9];
                if (flag2) {
                    src[9] = 0x42;
                }
            }
        }
        return v;
    }
}
