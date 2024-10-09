import java.awt.Toolkit;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.AWTException;
import java.awt.event.InputEvent;
import java.awt.Robot;

public class UnfocusableMaximizedFrameResizablity {

    private static Frame frame;

    private static Robot robot;

    private static boolean isProgInterruption = false;

    private static Thread mainThread = null;

    private static int sleepTime = 300000;

    private static void createAndShowFrame() throws Exception {
        if (System.getProperty("os.name").toLowerCase().startsWith("mac")) {
            cleanup();
            return;
        }
        if (!Toolkit.getDefaultToolkit().isFrameStateSupported(Frame.MAXIMIZED_BOTH)) {
            cleanup();
            return;
        }
        frame = new Frame("Unfocusable frame");
        frame.setMaximizedBounds(new Rectangle(0, 0, 300, 300));
        frame.setSize(200, 200);
        frame.setVisible(true);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setFocusableWindowState(false);
        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException("Robot creation failed");
        }
        robot.delay(2000);
        final Rectangle bounds = frame.getBounds();
        robot.mouseMove(bounds.x + bounds.width - 2, bounds.y + bounds.height - 2);
        robot.waitForIdle();
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.waitForIdle();
        robot.mouseMove(bounds.x + bounds.width + 20, bounds.y + bounds.height + 15);
        robot.waitForIdle();
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        robot.waitForIdle();
        final Rectangle finalBounds = frame.getBounds();
        if (!finalBounds.equals(bounds)) {
            cleanup();
            throw new RuntimeException("The maximized unfocusable frame can be resized.");
        }
        cleanup();
    }

    private static void cleanup() {
        if (frame != null) {
            frame.dispose();
        }
        isProgInterruption = true;
        mainThread.interrupt();
    }

    public static void main(String[] args) throws Exception {
        mainThread = Thread.currentThread();
        try {
            createAndShowFrame();
            mainThread.sleep(sleepTime);
        } catch (InterruptedException e) {
            if (!isProgInterruption) {
                throw e;
            }
        }
        if (!isProgInterruption) {
            throw new RuntimeException("Timed out after " + sleepTime / 1000 + " seconds");
        }
    }
}
