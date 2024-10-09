
package compiler.loopopts;

public class TestLoopLimitNodeElimination {

    private static class MyException extends RuntimeException {
    }

    private static final int ITERATIONS = 100000;

    private static final int SIZE = 400;

    private static int counter = 0;

    int[] array1 = new int[SIZE];

    void test() {
        int[] array2 = new int[SIZE];
        array1[2] = 0;
        array1 = array1;
        for (long i = 301; i > 2; i -= 2) {
            int j = 1;
            do {
                for (int k = (int) i; k < 1; k++) {
                }
            } while (++j < 4);
        }
        counter++;
        if (counter == ITERATIONS) {
            throw new MyException();
        }
    }

    public static void main(String[] args) {
        try {
            var test = new TestLoopLimitNodeElimination();
            while (true) {
                test.test();
            }
        } catch (MyException e) {
        }
    }
}
