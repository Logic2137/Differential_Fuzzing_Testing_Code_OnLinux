
package compiler.c1;

public class TestPinnedIntrinsics {

    private static void testNanoTime() {
        long start = System.nanoTime();
        long end = System.nanoTime();
        checkNanoTime(end - start);
    }

    private static void checkNanoTime(long diff) {
        if (diff < 0) {
            throw new RuntimeException("testNanoTime failed with " + diff);
        }
    }

    private static void testCurrentTimeMillis() {
        long start = System.currentTimeMillis();
        long end = System.currentTimeMillis();
        checkCurrentTimeMillis(end - start);
    }

    private static void checkCurrentTimeMillis(long diff) {
        if (diff < 0) {
            throw new RuntimeException("testCurrentTimeMillis failed with " + diff);
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100_000; ++i) {
            testNanoTime();
            testCurrentTimeMillis();
        }
    }
}
