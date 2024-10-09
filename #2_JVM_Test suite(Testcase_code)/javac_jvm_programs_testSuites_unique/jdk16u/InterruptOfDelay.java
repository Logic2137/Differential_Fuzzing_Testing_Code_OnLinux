

import java.awt.Robot;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;


public final class InterruptOfDelay {

    static final class DormantThread extends Thread {

        public void run() {
            final PrintStream old = System.err;
            final ByteArrayOutputStream err = new ByteArrayOutputStream();
            System.setErr(new PrintStream(err));
            try {
                new Robot().delay(10000);
            } catch (final Exception e) {
                throw new RuntimeException(e);
            } finally {
                System.setErr(old);
            }
            if (!err.toString().isBlank()) {
                throw new RuntimeException("Error stream is not empty: " + err);
            }
            if (!Thread.currentThread().isInterrupted()) {
                throw new RuntimeException("Thread was not interrupted");
            }
        }
    }

    public static void main(final String args[]) throws Exception {
        final Thread t = new DormantThread();
        t.start();
        Thread.sleep(1000);
        t.interrupt();
        t.join();
    }
}
