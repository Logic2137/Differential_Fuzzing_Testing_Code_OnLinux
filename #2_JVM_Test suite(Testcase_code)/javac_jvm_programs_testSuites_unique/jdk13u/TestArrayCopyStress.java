import java.util.concurrent.*;

public class TestArrayCopyStress {

    private static final int ARRAY_SIZE = 1000;

    private static final int ITERATIONS = 10000;

    static class Foo {

        int num;

        Foo(int num) {
            this.num = num;
        }
    }

    static class Bar {
    }

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < ITERATIONS; i++) {
            testConjoint();
        }
    }

    private static void testConjoint() {
        Foo[] array = new Foo[ARRAY_SIZE];
        for (int i = 0; i < ARRAY_SIZE; i++) {
            array[i] = new Foo(i);
        }
        int src_idx = ThreadLocalRandom.current().nextInt(0, ARRAY_SIZE);
        int dst_idx = ThreadLocalRandom.current().nextInt(0, ARRAY_SIZE);
        int len = ThreadLocalRandom.current().nextInt(0, Math.min(ARRAY_SIZE - src_idx, ARRAY_SIZE - dst_idx));
        System.arraycopy(array, src_idx, array, dst_idx, len);
        for (int i = 0; i < ARRAY_SIZE; i++) {
            if (i >= dst_idx && i < dst_idx + len) {
                assertEquals(array[i].num, i - (dst_idx - src_idx));
            } else {
                assertEquals(array[i].num, i);
            }
        }
    }

    private static void assertEquals(int a, int b) {
        if (a != b)
            throw new RuntimeException("assert failed");
    }
}
