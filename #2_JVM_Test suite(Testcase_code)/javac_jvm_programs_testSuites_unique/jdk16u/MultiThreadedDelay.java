

import java.awt.Robot;
import java.util.concurrent.CountDownLatch;


public final class MultiThreadedDelay {

    private static volatile boolean complete;

    public static void main(final String[] args) throws Exception {
        CountDownLatch go = new CountDownLatch(1);
        Robot robot = new Robot();
        Thread bigDelay = new Thread(() -> {
            go.countDown();
            robot.delay(20000);
            complete = true;
        });
        bigDelay.start();
        go.await();
        robot.delay(1000);
        if (complete) {
            throw new RuntimeException("Delay is too slow");
        }
        bigDelay.interrupt();
    }
}
