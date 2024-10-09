

import java.awt.Frame;
import java.awt.Robot;
import java.util.concurrent.TimeUnit;


public final class InfiniteLoopException {

    public static void main(String[] args) throws Exception {
        Frame frame = new Frame();
        try {
            frame.setSize(300, 300);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            test(frame);
        } finally {
            frame.dispose();
        }
    }

    private static void test(Frame frame) throws Exception {
        Runnable repaint = () -> {
            while (frame.isDisplayable()) {
                frame.repaint();
            }
        };
        new Thread(repaint).start();
        new Thread(repaint).start();
        new Thread(repaint).start();

        Robot robot = new Robot();
        long start = System.nanoTime();
        robot.waitForIdle();
        long time = TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - start);
        if (time > 20) {
            throw new RuntimeException("Too slow:" + time);
        }
    }
}
