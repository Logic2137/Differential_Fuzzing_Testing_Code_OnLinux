



import java.util.Arrays;
import java.util.concurrent.*;

public class TestPinnedGarbage {
    static {
        System.loadLibrary("TestPinnedGarbage");
    }

    private static final int NUM_RUNS      = 1_000;
    private static final int OBJS_COUNT    = 1_000;
    private static final int GARBAGE_COUNT = 1_000_000;

    private static native void pin(int[] a);
    private static native void unpin(int[] a);

    public static void main(String[] args) {
        for (int i = 0; i < NUM_RUNS; i++) {
            test();
        }
    }

    private static void test() {
        Object[] objs = new Object[OBJS_COUNT];
        for (int i = 0; i < OBJS_COUNT; i++) {
            objs[i] = new MyClass();
        }

        int[] cog = new int[10];
        int cogIdx = ThreadLocalRandom.current().nextInt(OBJS_COUNT);
        objs[cogIdx] = cog;
        pin(cog);

        for (int i = 0; i < GARBAGE_COUNT; i++) {
            int rIdx = ThreadLocalRandom.current().nextInt(OBJS_COUNT);
            if (rIdx != cogIdx) {
                objs[rIdx] = new MyClass();
            }
        }

        unpin(cog);
    }

    public static class MyClass {
        public Object ref = new Object();
    }

}
