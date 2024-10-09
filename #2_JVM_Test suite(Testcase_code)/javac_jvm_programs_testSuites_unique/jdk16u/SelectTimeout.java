


import java.io.IOException;
import java.nio.channels.Selector;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class SelectTimeout {
    private static final long BIG_TIMEOUT    = 100_000_001_000L; 
    private static final long BIGGER_TIMEOUT = 850_000_000_000_000L; 
    private static final long SLEEP_MILLIS   = 10_000;

    public static void main(String[] args)
        throws IOException, InterruptedException {
        int failures = 0;
        long[] timeouts =
            new long[] {1, BIG_TIMEOUT/2, BIG_TIMEOUT - 1, BIG_TIMEOUT,
                BIGGER_TIMEOUT};
        for (long t : timeouts) {
            if (!test(t)) {
                failures++;
            }
        }
        if (failures > 0) {
            throw new RuntimeException("Test failed!");
        } else {
            System.out.println("Test succeeded");
        }
    }

    private static boolean test(final long timeout)
        throws InterruptedException, IOException {
        AtomicReference<Exception> theException =
            new AtomicReference<>();
        AtomicBoolean isTimedOut = new AtomicBoolean();

        Selector selector = Selector.open();

        Thread t = new Thread(() -> {
            try {
                selector.select(timeout);
                isTimedOut.set(true);
            } catch (IOException ioe) {
                theException.set(ioe);
            }
        });
        t.start();

        t.join(SLEEP_MILLIS);

        boolean result;
        if (theException.get() == null) {
            if (timeout > SLEEP_MILLIS && isTimedOut.get()) {
                System.err.printf("Test timed out early with timeout %d%n",
                    timeout);
                result = false;
            } else {
                System.out.printf("Test succeeded with timeout %d%n", timeout);
                result = true;
            }
        } else {
            System.err.printf("Test failed with timeout %d%n", timeout);
            theException.get().printStackTrace();
            result = false;
        }

        t.interrupt();
        selector.close();

        return result;
    }
}
