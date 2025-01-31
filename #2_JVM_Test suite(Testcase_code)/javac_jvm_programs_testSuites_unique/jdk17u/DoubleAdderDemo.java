import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;
import java.util.concurrent.atomic.DoubleAdder;

public class DoubleAdderDemo {

    static final int INCS_PER_THREAD = 10000000;

    static final int NCPU = Runtime.getRuntime().availableProcessors();

    static final int SHORT_RUN_MAX_THREADS = NCPU > 1 ? NCPU / 2 : 1;

    static final int LONG_RUN_MAX_THREADS = NCPU * 2;

    static final ExecutorService pool = Executors.newCachedThreadPool();

    static final class SynchronizedDoubleAdder {

        double value;

        synchronized double sum() {
            return value;
        }

        synchronized void add(double x) {
            value += x;
        }
    }

    public static void main(String[] args) {
        boolean shortRun = args.length > 0 && args[0].equals("-shortrun");
        int maxNumThreads = shortRun ? SHORT_RUN_MAX_THREADS : LONG_RUN_MAX_THREADS;
        System.out.println("Warmup...");
        int half = NCPU > 1 ? NCPU / 2 : 1;
        if (!shortRun)
            syncTest(half, 1000);
        adderTest(half, 1000);
        for (int reps = 0; reps < 2; ++reps) {
            System.out.println("Running...");
            for (int i = 1; i <= maxNumThreads; i <<= 1) {
                if (!shortRun)
                    syncTest(i, INCS_PER_THREAD);
                adderTest(i, INCS_PER_THREAD);
            }
        }
        pool.shutdown();
    }

    static void syncTest(int nthreads, int incs) {
        System.out.print("Synchronized ");
        Phaser phaser = new Phaser(nthreads + 1);
        SynchronizedDoubleAdder a = new SynchronizedDoubleAdder();
        for (int i = 0; i < nthreads; ++i) pool.execute(new SyncTask(a, phaser, incs));
        report(nthreads, incs, timeTasks(phaser), a.sum());
    }

    static void adderTest(int nthreads, int incs) {
        System.out.print("DoubleAdder  ");
        Phaser phaser = new Phaser(nthreads + 1);
        DoubleAdder a = new DoubleAdder();
        for (int i = 0; i < nthreads; ++i) pool.execute(new AdderTask(a, phaser, incs));
        report(nthreads, incs, timeTasks(phaser), a.sum());
    }

    static void report(int nthreads, int incs, long time, double sum) {
        long total = (long) nthreads * incs;
        if (sum != (double) total)
            throw new Error(sum + " != " + total);
        double secs = (double) time / 1000_000_000L;
        long rate = total * 1000L / time;
        System.out.printf("threads:%3d  Time: %7.3fsec  Incs per microsec: %4d\n", nthreads, secs, rate);
    }

    static long timeTasks(Phaser phaser) {
        phaser.arriveAndAwaitAdvance();
        long start = System.nanoTime();
        phaser.arriveAndAwaitAdvance();
        phaser.arriveAndAwaitAdvance();
        return System.nanoTime() - start;
    }

    static final class AdderTask implements Runnable {

        final DoubleAdder adder;

        final Phaser phaser;

        final int incs;

        volatile double result;

        AdderTask(DoubleAdder adder, Phaser phaser, int incs) {
            this.adder = adder;
            this.phaser = phaser;
            this.incs = incs;
        }

        public void run() {
            phaser.arriveAndAwaitAdvance();
            phaser.arriveAndAwaitAdvance();
            DoubleAdder a = adder;
            for (int i = 0; i < incs; ++i) a.add(1.0);
            result = a.sum();
            phaser.arrive();
        }
    }

    static final class SyncTask implements Runnable {

        final SynchronizedDoubleAdder adder;

        final Phaser phaser;

        final int incs;

        volatile double result;

        SyncTask(SynchronizedDoubleAdder adder, Phaser phaser, int incs) {
            this.adder = adder;
            this.phaser = phaser;
            this.incs = incs;
        }

        public void run() {
            phaser.arriveAndAwaitAdvance();
            phaser.arriveAndAwaitAdvance();
            SynchronizedDoubleAdder a = adder;
            for (int i = 0; i < incs; ++i) a.add(1.0);
            result = a.sum();
            phaser.arrive();
        }
    }
}
