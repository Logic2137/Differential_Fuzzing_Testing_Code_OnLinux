import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import static java.util.concurrent.TimeUnit.DAYS;
import java.util.concurrent.atomic.AtomicReference;

public class SwallowedInterruptedException {

    static final int ITERATIONS = 100;

    public static void main(String[] args) throws Throwable {
        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        for (int i = 1; i <= ITERATIONS; i++) {
            boolean timed = rnd.nextBoolean();
            long sleepMillis = rnd.nextLong(10);
            CompletableFuture<Void> future = new CompletableFuture<>();
            CountDownLatch threadRunning = new CountDownLatch(1);
            AtomicReference<Throwable> fail = new AtomicReference<>();
            Thread thread = new Thread(() -> {
                threadRunning.countDown();
                try {
                    Void result = (timed) ? future.get(1, DAYS) : future.get();
                    if (!Thread.currentThread().isInterrupted()) {
                        fail.set(new AssertionError("Future.get completed with interrupt status not set"));
                    }
                } catch (InterruptedException ex) {
                    if (Thread.currentThread().isInterrupted()) {
                        fail.set(new AssertionError("InterruptedException with interrupt status set"));
                    }
                } catch (Throwable ex) {
                    fail.set(ex);
                }
            });
            thread.start();
            threadRunning.await();
            thread.interrupt();
            if (sleepMillis > 0)
                Thread.sleep(sleepMillis);
            future.complete(null);
            thread.join();
            if (fail.get() != null) {
                throw new AssertionError(String.format("Test failed at iteration %d with [timed=%s sleepMillis=%d]", i, timed, sleepMillis), fail.get());
            }
        }
    }
}
