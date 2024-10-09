

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadLocalRandom;
import static java.util.concurrent.TimeUnit.DAYS;






public class LostInterrupt {
    static final int ITERATIONS = 10_000;

    public static void main(String[] args) throws Exception {
        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        ForkJoinPool executor = new ForkJoinPool(1);
        try {
            for (int i = 0; i < ITERATIONS; i++) {
                CompletableFuture<String> future = new CompletableFuture<>();
                boolean timed = rnd.nextBoolean();
                executor.execute(() -> future.complete("foo"));

                Thread.currentThread().interrupt();
                try {
                    String result = timed ? future.get(1, DAYS) : future.get();

                    if (!Thread.interrupted())
                        throw new AssertionError("lost interrupt, run=" + i);
                } catch (InterruptedException expected) {
                    if (Thread.interrupted())
                        throw new AssertionError(
                            "interrupt status not cleared, run=" + i);
                }
            }
        } finally {
            executor.shutdown();
        }
    }
}
