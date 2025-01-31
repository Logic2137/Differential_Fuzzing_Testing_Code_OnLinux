import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

public class Invoke {

    static volatile int passed = 0, failed = 0;

    static void fail(String msg) {
        failed++;
        new AssertionError(msg).printStackTrace();
    }

    static void pass() {
        passed++;
    }

    static void unexpected(Throwable t) {
        failed++;
        t.printStackTrace();
    }

    static void check(boolean condition, String msg) {
        if (condition)
            pass();
        else
            fail(msg);
    }

    static void check(boolean condition) {
        check(condition, "Assertion failure");
    }

    static long secondsElapsedSince(long startTime) {
        return NANOSECONDS.toSeconds(System.nanoTime() - startTime);
    }

    static void awaitInterrupt(long timeoutSeconds) {
        long startTime = System.nanoTime();
        try {
            Thread.sleep(SECONDS.toMillis(timeoutSeconds));
            fail("timed out waiting for interrupt");
        } catch (InterruptedException expected) {
            check(secondsElapsedSince(startTime) < timeoutSeconds);
        }
    }

    public static void main(String[] args) {
        try {
            testInvokeAll();
            testInvokeAny();
            testInvokeAny_cancellationInterrupt();
        } catch (Throwable t) {
            unexpected(t);
        }
        if (failed > 0)
            throw new Error(String.format("Passed = %d, failed = %d", passed, failed));
    }

    static final long timeoutSeconds = 10L;

    static void testInvokeAll() throws Throwable {
        final ThreadLocalRandom rnd = ThreadLocalRandom.current();
        final int nThreads = rnd.nextInt(2, 7);
        final boolean timed = rnd.nextBoolean();
        final ExecutorService pool = Executors.newFixedThreadPool(nThreads);
        final AtomicLong count = new AtomicLong(0);
        class Task implements Callable<Long> {

            public Long call() throws Exception {
                return count.incrementAndGet();
            }
        }
        try {
            final List<Task> tasks = IntStream.range(0, nThreads).mapToObj(i -> new Task()).collect(Collectors.toList());
            List<Future<Long>> futures;
            if (timed) {
                long startTime = System.nanoTime();
                futures = pool.invokeAll(tasks, timeoutSeconds, SECONDS);
                check(secondsElapsedSince(startTime) < timeoutSeconds);
            } else
                futures = pool.invokeAll(tasks);
            check(futures.size() == tasks.size());
            check(count.get() == tasks.size());
            long gauss = 0;
            for (Future<Long> future : futures) gauss += future.get();
            check(gauss == (tasks.size() + 1) * tasks.size() / 2);
            pool.shutdown();
            check(pool.awaitTermination(10L, SECONDS));
        } finally {
            pool.shutdownNow();
        }
    }

    static void testInvokeAny() throws Throwable {
        final ThreadLocalRandom rnd = ThreadLocalRandom.current();
        final boolean timed = rnd.nextBoolean();
        final ExecutorService pool = Executors.newSingleThreadExecutor();
        final AtomicLong count = new AtomicLong(0);
        final CountDownLatch invokeAnyDone = new CountDownLatch(1);
        class Task implements Callable<Long> {

            public Long call() throws Exception {
                long x = count.incrementAndGet();
                check(x <= 2);
                if (x == 2) {
                    awaitInterrupt(timeoutSeconds);
                    check(invokeAnyDone.await(timeoutSeconds, SECONDS));
                }
                return x;
            }
        }
        try {
            final List<Task> tasks = IntStream.range(0, rnd.nextInt(1, 7)).mapToObj(i -> new Task()).collect(Collectors.toList());
            long val;
            if (timed) {
                long startTime = System.nanoTime();
                val = pool.invokeAny(tasks, timeoutSeconds, SECONDS);
                check(secondsElapsedSince(startTime) < timeoutSeconds);
            } else
                val = pool.invokeAny(tasks);
            check(val == 1);
            invokeAnyDone.countDown();
            check(count.get() == 1 || count.get() == 2);
            pool.shutdown();
            check(pool.awaitTermination(timeoutSeconds, SECONDS));
        } finally {
            pool.shutdownNow();
        }
    }

    static void testInvokeAny_cancellationInterrupt() throws Throwable {
        final ThreadLocalRandom rnd = ThreadLocalRandom.current();
        final int nThreads = rnd.nextInt(2, 7);
        final boolean timed = rnd.nextBoolean();
        final ExecutorService pool = Executors.newFixedThreadPool(nThreads);
        final AtomicLong count = new AtomicLong(0);
        final AtomicLong interruptedCount = new AtomicLong(0);
        final CyclicBarrier allStarted = new CyclicBarrier(nThreads);
        class Task implements Callable<Long> {

            public Long call() throws Exception {
                allStarted.await();
                long x = count.incrementAndGet();
                if (x > 1)
                    awaitInterrupt(timeoutSeconds);
                return x;
            }
        }
        try {
            final List<Task> tasks = IntStream.range(0, nThreads).mapToObj(i -> new Task()).collect(Collectors.toList());
            long val;
            if (timed) {
                long startTime = System.nanoTime();
                val = pool.invokeAny(tasks, timeoutSeconds, SECONDS);
                check(secondsElapsedSince(startTime) < timeoutSeconds);
            } else
                val = pool.invokeAny(tasks);
            check(val == 1);
            pool.shutdown();
            check(pool.awaitTermination(timeoutSeconds, SECONDS));
            check(count.get() == nThreads);
        } finally {
            pool.shutdownNow();
        }
    }
}
