

import java.awt.*;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.InputEvent;



public class SlideNotResizableTest {

    private static volatile boolean passed = false;
    private static final Dimension FRAME_SIZE = new Dimension(100, 100);
    private static final Point FRAME_LOCATION = new Point(200, 200);

    public static void main(String[] args) throws Throwable {
        Frame aFrame = null;
        try {
            aFrame = new Frame();
            aFrame.setSize(FRAME_SIZE);
            aFrame.setLocation(FRAME_LOCATION);
            aFrame.setResizable(false);
            aFrame.setVisible(true);

            sync();

            if (!aFrame.getLocation().equals(FRAME_LOCATION)) {
                throw new RuntimeException("FAILED: Wrong frame position");
            }
        } finally {
            if (aFrame != null) {
                aFrame.dispose();
            }
        }
    }

    private static void sync() throws Exception {
        Robot robot = new Robot();
        robot.waitForIdle();
        Thread.sleep(1000);
    }
}
