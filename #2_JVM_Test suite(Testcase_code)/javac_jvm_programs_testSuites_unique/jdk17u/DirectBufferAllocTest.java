import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DirectBufferAllocTest {

    static final int RUN_TIME_SECONDS = 5;

    static final int MIN_THREADS = 4;

    static final int MAX_THREADS = 64;

    static final int CAPACITY = 1024 * 1024;

    public static void main(String[] args) throws Exception {
        int runTimeSeconds = RUN_TIME_SECONDS;
        int threads = Math.max(Math.min(Runtime.getRuntime().availableProcessors() * 2, MAX_THREADS), MIN_THREADS);
        int capacity = CAPACITY;
        int printBatchSize = 0;
        for (int i = 0; i < args.length; i++) {
            switch(args[i]) {
                case "-r":
                    runTimeSeconds = Integer.parseInt(args[++i]);
                    break;
                case "-t":
                    threads = Integer.parseInt(args[++i]);
                    break;
                case "-c":
                    capacity = Integer.parseInt(args[++i]);
                    break;
                case "-p":
                    printBatchSize = Integer.parseInt(args[++i]);
                    break;
                default:
                    System.err.println("Usage: java" + " [-XX:MaxDirectMemorySize=XXXm]" + " DirectBufferAllocTest" + " [-r run-time-seconds]" + " [-t threads]" + " [-c capacity-of-direct-buffers]" + " [-p print-alloc-time-batch-size]");
                    System.exit(-1);
            }
        }
        System.out.printf("Allocating direct ByteBuffers with capacity %d bytes, using %d threads for %d seconds...\n", capacity, threads, runTimeSeconds);
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        int pbs = printBatchSize;
        int cap = capacity;
        List<Future<Void>> futures = IntStream.range(0, threads).mapToObj(i -> (Callable<Void>) () -> {
            long t0 = System.nanoTime();
            loop: while (true) {
                for (int n = 0; pbs == 0 || n < pbs; n++) {
                    if (Thread.interrupted()) {
                        break loop;
                    }
                    ByteBuffer.allocateDirect(cap);
                }
                long t1 = System.nanoTime();
                if (pbs > 0) {
                    System.out.printf("Thread %2d: %5.2f ms/allocation\n", i, ((double) (t1 - t0) / (1_000_000d * pbs)));
                }
                t0 = t1;
            }
            return null;
        }).map(executor::submit).collect(Collectors.toList());
        for (int i = 0; i < runTimeSeconds; i++) {
            if (futures.stream().anyMatch(Future::isDone)) {
                break;
            }
            Thread.sleep(1000L);
        }
        Exception exception = null;
        for (Future<Void> future : futures) {
            if (future.isDone()) {
                try {
                    future.get();
                } catch (ExecutionException e) {
                    if (exception == null) {
                        exception = new RuntimeException("Errors encountered!");
                    }
                    exception.addSuppressed(e.getCause());
                }
            } else {
                future.cancel(true);
            }
        }
        executor.shutdown();
        if (exception != null) {
            throw exception;
        } else {
            System.out.printf("No errors after %d seconds.\n", runTimeSeconds);
        }
    }
}
