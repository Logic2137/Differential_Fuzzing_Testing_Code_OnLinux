



public class TestOuterStripMinedDeadAfterExpansion {
    private static int field;

    public static void main(String[] args) {
        int[] array = new int[100];
        for (int i = 0; i < 20_000; i++) {
            test(array, array, array.length);
            test_helper(array, 100);
        }
    }

    private static int test(int[] src, int[] dst, int length) {
        field = 4 << 17;
        System.arraycopy(src, 0, dst, 0, length);
        int stop = field >>> 17;
        return test_helper(dst, stop);
    }

    private static int test_helper(int[] dst, int stop) {
        int res = 0;
        for (int i = 0; i < stop; i++) {
            res += dst[i];
        }
        return res;
    }
}
