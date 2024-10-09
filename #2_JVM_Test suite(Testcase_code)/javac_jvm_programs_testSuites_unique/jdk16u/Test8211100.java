



package compiler.c1;

public class Test8211100 {
    private static final int ITERATIONS = 100_000;

    public static void main(String[] args) {
        for (int i = 0; i < ITERATIONS; i++) {
            test(4558828911L,
                 4294967296L);
        }
    }

    private static void test(long one, long two) {
        while (true) {
            if (one >= two) {
                break;
            }
        }
    }
}
