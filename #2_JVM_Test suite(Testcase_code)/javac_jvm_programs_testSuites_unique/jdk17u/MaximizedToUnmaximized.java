import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;

public class MaximizedToUnmaximized {

    public static void main(String[] args) throws Exception {
        testFrame(false);
        testFrame(true);
    }

    static void testFrame(boolean isUndecorated) throws Exception {
        Frame frame = new Frame();
        try {
            Robot robot = new Robot();
            robot.setAutoDelay(100);
            frame.setUndecorated(isUndecorated);
            GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
            Rectangle bounds = gc.getBounds();
            Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(gc);
            int x = bounds.x + insets.left;
            int y = bounds.y + insets.top;
            int width = bounds.width - insets.left - insets.right;
            int height = bounds.height - insets.top - insets.bottom;
            Rectangle rect = new Rectangle(x, y, width, height);
            frame.pack();
            frame.setBounds(rect);
            frame.setVisible(true);
            robot.waitForIdle();
            robot.delay(500);
            if (frame.getWidth() <= width / 2 || frame.getHeight() <= height / 2) {
                throw new RuntimeException("Frame size is small!");
            }
            if (!isUndecorated && frame.getExtendedState() != Frame.MAXIMIZED_BOTH) {
                throw new RuntimeException("Frame state does not equal" + " MAXIMIZED_BOTH!");
            }
        } finally {
            frame.dispose();
        }
    }
}
