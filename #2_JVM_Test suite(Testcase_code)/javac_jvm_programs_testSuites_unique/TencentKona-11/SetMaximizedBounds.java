

import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;


public class SetMaximizedBounds {

    public static void main(String[] args) throws Exception {
        
        String os = System.getProperty("os.name").toLowerCase();
        if (!os.contains("windows") && !os.contains("os x")) {
            return;
        }

        if (!Toolkit.getDefaultToolkit().
                isFrameStateSupported(Frame.MAXIMIZED_BOTH)) {
            return;
        }

        GraphicsEnvironment ge = GraphicsEnvironment.
                getLocalGraphicsEnvironment();

        for (GraphicsDevice gd : ge.getScreenDevices()) {
            for (GraphicsConfiguration gc : gd.getConfigurations()) {
                testMaximizedBounds(gc, false);
                testMaximizedBounds(gc, true);
            }
        }
    }

    static void testMaximizedBounds(GraphicsConfiguration gc, boolean undecorated)
            throws Exception {

        Frame frame = null;
        try {

            Rectangle maxArea = getMaximizedScreenArea(gc);

            Robot robot = new Robot();
            robot.setAutoDelay(50);

            frame = new Frame();
            frame.setUndecorated(undecorated);
            Rectangle maximizedBounds = new Rectangle(
                    maxArea.x + maxArea.width / 5,
                    maxArea.y + maxArea.height / 5,
                    maxArea.width / 2,
                    maxArea.height / 2);
            frame.setMaximizedBounds(maximizedBounds);
            frame.setSize(maxArea.width / 8, maxArea.height / 8);
            frame.setVisible(true);
            robot.waitForIdle();

            frame.setExtendedState(Frame.MAXIMIZED_BOTH);
            robot.waitForIdle();
            robot.delay(1000);

            Rectangle bounds = frame.getBounds();
            if (!bounds.equals(maximizedBounds)) {
                System.err.println("Expected: " + maximizedBounds);
                System.err.println("Actual: " + bounds);
                throw new RuntimeException("The bounds of the Frame do not equal to what"
                        + " is specified when the frame is in Frame.MAXIMIZED_BOTH state");
            }

            frame.setExtendedState(Frame.NORMAL);
            robot.waitForIdle();
            robot.delay(1000);

            maximizedBounds = new Rectangle(
                    maxArea.x + maxArea.width / 6,
                    maxArea.y + maxArea.height / 6,
                    maxArea.width / 3,
                    maxArea.height / 3);
            frame.setMaximizedBounds(maximizedBounds);
            frame.setExtendedState(Frame.MAXIMIZED_BOTH);
            robot.waitForIdle();
            robot.delay(1000);

            bounds = frame.getBounds();
            if (!bounds.equals(maximizedBounds)) {
                System.err.println("Expected: " + maximizedBounds);
                System.err.println("Actual: " + bounds);
                throw new RuntimeException("The bounds of the Frame do not equal to what"
                        + " is specified when the frame is in Frame.MAXIMIZED_BOTH state");
            }
        } finally {
            if (frame != null) {
                frame.dispose();
            }
        }
    }

    static Rectangle getMaximizedScreenArea(GraphicsConfiguration gc) {
        Rectangle bounds = gc.getBounds();
        Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(gc);
        return new Rectangle(
                bounds.x + insets.left,
                bounds.y + insets.top,
                bounds.width - insets.left - insets.right,
                bounds.height - insets.top - insets.bottom);
    }
}
