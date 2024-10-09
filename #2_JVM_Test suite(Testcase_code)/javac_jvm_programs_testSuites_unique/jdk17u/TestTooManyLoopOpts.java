public class TestTooManyLoopOpts {

    private static volatile int field;

    public static void main(String[] args) {
        test(0);
    }

    private static void test(int stop) {
        for (long l = 0; l < 10; l++) {
            test_helper(stop, 26);
            field = 0x42;
        }
    }

    private static void test_helper(int stop, int rec) {
        if (rec <= 0) {
            return;
        }
        for (int i = 0; i < stop; i++) {
            test_helper(stop, rec - 1);
        }
    }
}
