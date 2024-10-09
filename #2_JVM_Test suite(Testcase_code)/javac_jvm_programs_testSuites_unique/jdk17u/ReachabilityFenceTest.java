import java.lang.ref.Reference;
import java.util.concurrent.atomic.AtomicBoolean;

public class ReachabilityFenceTest {

    static final int LOOP_ITERS = Integer.getInteger("LOOP_ITERS", 50000);

    static final int WARMUP_LOOP_ITERS = LOOP_ITERS - Integer.getInteger("GC_ITERS", 100);

    static final int MAIN_ITERS = 3;

    static final boolean PREMATURE_FINALIZATION = Boolean.getBoolean("premature");

    public static void main(String... args) {
        boolean finalized = false;
        for (int c = 0; !finalized && c < MAIN_ITERS; c++) {
            finalized |= nonFenced();
        }
        if (PREMATURE_FINALIZATION && !finalized) {
            throw new IllegalStateException("The object had never been finalized before timeout reached.");
        }
        if (!PREMATURE_FINALIZATION && finalized) {
            throw new IllegalStateException("The object had been finalized without a fence, even though we don't expect it.");
        }
        if (!PREMATURE_FINALIZATION)
            return;
        finalized = false;
        for (int c = 0; !finalized && c < MAIN_ITERS; c++) {
            finalized |= fenced();
        }
        if (finalized) {
            throw new IllegalStateException("The object had been prematurely finalized.");
        }
    }

    public static boolean nonFenced() {
        AtomicBoolean finalized = new AtomicBoolean();
        MyFinalizeable o = new MyFinalizeable(finalized);
        for (int i = 0; i < LOOP_ITERS; i++) {
            if (finalized.get())
                break;
            if (i > WARMUP_LOOP_ITERS) {
                System.gc();
                System.runFinalization();
            }
        }
        return finalized.get();
    }

    public static boolean fenced() {
        AtomicBoolean finalized = new AtomicBoolean();
        MyFinalizeable o = new MyFinalizeable(finalized);
        for (int i = 0; i < LOOP_ITERS; i++) {
            if (finalized.get())
                break;
            if (i > WARMUP_LOOP_ITERS) {
                System.gc();
                System.runFinalization();
            }
        }
        try {
            return finalized.get();
        } finally {
            Reference.reachabilityFence(o);
        }
    }

    private static class MyFinalizeable {

        private final AtomicBoolean finalized;

        public MyFinalizeable(AtomicBoolean b) {
            this.finalized = b;
        }

        @Override
        protected void finalize() throws Throwable {
            super.finalize();
            finalized.set(true);
        }
    }
}
