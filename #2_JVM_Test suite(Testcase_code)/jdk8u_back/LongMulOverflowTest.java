public class LongMulOverflowTest {

    public static void main(String[] args) {
        LongMulOverflowTest test = new LongMulOverflowTest();
        for (int i = 0; i < 10; ++i) {
            try {
                test.runTest();
                throw new RuntimeException("Error, runTest() did not overflow!");
            } catch (ArithmeticException e) {
            }
            try {
                test.runTestOverflow();
                throw new RuntimeException("Error, runTestOverflow() did not overflow!");
            } catch (ArithmeticException e) {
            }
        }
    }

    public void runTest() {
        java.lang.Math.multiplyExact(Long.MIN_VALUE, 7);
    }

    public void runTestOverflow() {
        java.lang.Math.multiplyExact((Long.MAX_VALUE / 2) + 1, 2);
    }
}
