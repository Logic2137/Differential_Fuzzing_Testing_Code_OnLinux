import java.awt.Robot;
import java.util.concurrent.CountDownLatch;
import javax.swing.SwingUtilities;

public final class WaitForIdleSyncroizedOnString {

    private static final String WAIT_LOCK = "Wait Lock";

    private static volatile boolean passed = true;

    public static void main(final String[] args) throws Exception {
        CountDownLatch go = new CountDownLatch(1);
        Robot r = new Robot();
        SwingUtilities.invokeLater(() -> System.out.println("some work"));
        Thread t = new Thread(() -> {
            synchronized (WAIT_LOCK) {
                go.countDown();
                try {
                    Thread.sleep(30000);
                    passed = false;
                } catch (InterruptedException e) {
                    System.out.println("e = " + e);
                }
            }
        });
        t.start();
        go.await();
        r.waitForIdle();
        t.interrupt();
        if (!passed) {
            throw new RuntimeException("Test failed");
        }
    }
}
