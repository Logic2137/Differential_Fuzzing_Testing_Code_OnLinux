public class TestLoadPinnedAfterAllocate {

    private int field;

    private static volatile int barrier;

    private static Object field2;

    public static void main(String[] args) {
        final TestLoadPinnedAfterAllocate test = new TestLoadPinnedAfterAllocate();
        for (int i = 0; i < 20_000; i++) {
            test.test(1, 10);
        }
    }

    private int test(int start, int stop) {
        int[] array = new int[10];
        for (int j = 0; j < 10; j++) {
            barrier = 1;
            for (int i = 1; i < 10; i *= 2) {
                field2 = array;
                array = new int[10];
            }
        }
        int v = field;
        array[0] = v;
        return v + v;
    }
}
