
package compiler.vectorization;

public class TestPopCountVector {

    private int[] input;

    private int[] output;

    private static final int LEN = 1024;

    public static void main(String[] args) {
        TestPopCountVector test = new TestPopCountVector();
        for (int i = 0; i < 10_000; ++i) {
            test.vectorizeBitCount();
        }
        System.out.println("Checking popcount result");
        test.checkResult();
        for (int i = 0; i < 10_000; ++i) {
            test.vectorizeBitCount();
        }
        System.out.println("Checking popcount result");
        test.checkResult();
    }

    public TestPopCountVector() {
        input = new int[LEN];
        output = new int[LEN];
        for (int i = 0; i < LEN; ++i) {
            input[i] = i % 2 == 0 ? i : -1 * i;
        }
    }

    public void vectorizeBitCount() {
        for (int i = 0; i < LEN; ++i) {
            output[i] = Integer.bitCount(input[i]);
        }
    }

    public void checkResult() {
        for (int i = 0; i < LEN; ++i) {
            int expected = Integer.bitCount(input[i]);
            if (output[i] != expected) {
                throw new RuntimeException("Invalid result: output[" + i + "] = " + output[i] + " != " + expected);
            }
        }
    }
}
