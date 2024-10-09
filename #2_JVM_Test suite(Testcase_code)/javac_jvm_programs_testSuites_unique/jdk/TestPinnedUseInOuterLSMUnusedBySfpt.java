



public class TestPinnedUseInOuterLSMUnusedBySfpt {
    public static void main(String[] args) {
        int[] array = new int[10000];
        for (int i = 0; i < 20_000; i++) {
            test(100, array, 42);
            test(100, array, 0);
        }
    }

    private static float test(int stop, int[] array, int val) {
        if (array == null) {
        }
        int j;
        for (j = 0; j < 10; j++) {

        }
        int i;
        int v = 0;
        float f = 1;
        for (i = 0; i < 10000; i++) {
            if ((j - 10) * i + val == 42) {
                f *= 2;
            } else {
                f *= 3;
            }
            v = (j - 10) * array[i];
            if (i % 10001 != i) {
                return v;
            }
        }
        return v + array[i-1] + f;
    }
}
