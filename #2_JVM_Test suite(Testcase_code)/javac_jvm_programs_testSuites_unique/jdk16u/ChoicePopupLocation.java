

import java.awt.Choice;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;


public final class ChoicePopupLocation {

    private static final int SIZE = 350;
    private static int frameWidth;

    public static void main(final String[] args) throws Exception {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] sds = ge.getScreenDevices();
        Point left = null;
        Point right = null;
        for (GraphicsDevice sd : sds) {
            GraphicsConfiguration gc = sd.getDefaultConfiguration();
            Rectangle bounds = gc.getBounds();
            if (left == null || left.x > bounds.x) {
                left = new Point(bounds.x, bounds.y + bounds.height / 2);
            }
            if (right == null || right.x < bounds.x + bounds.width) {
                right = new Point(bounds.x + bounds.width,
                                  bounds.y + bounds.height / 2);
            }

            Point point = new Point(bounds.x, bounds.y);
            Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(gc);
            while (point.y < bounds.y + bounds.height - insets.bottom - SIZE ) {
                while (point.x < bounds.x + bounds.width - insets.right - SIZE) {
                    test(point);
                    point.translate(bounds.width / 5, 0);
                }
                point.setLocation(bounds.x, point.y + bounds.height / 5);
            }

        }
        if (left != null) {
            left.translate(-50, 0);
            test(left);
        }
        if (right != null) {
            right.translate(-frameWidth + 50, 0);
            test(right);
        }
    }

    private static void test(final Point tmp) throws Exception {
        Choice choice = new Choice();
        for (int i = 1; i < 7; i++) {
            choice.add("Long-long-long-long-long text in the item-" + i);
        }
        Frame frame = new Frame();
        try {
            frame.setAlwaysOnTop(true);
            frame.setLayout(new FlowLayout());
            frame.add(choice);
            frame.pack();
            frameWidth = frame.getWidth();
            frame.setSize(frameWidth, SIZE);
            frame.setVisible(true);
            frame.setLocation(tmp.x, tmp.y);
            openPopup(choice);
        } finally {
            frame.dispose();
        }
    }

    private static void openPopup(final Choice choice) throws Exception {
        Robot robot = new Robot();
        robot.setAutoDelay(100);
        robot.setAutoWaitForIdle(true);
        robot.waitForIdle();
        Point pt = choice.getLocationOnScreen();
        robot.mouseMove(pt.x + choice.getWidth() / 2,
                        pt.y + choice.getHeight() / 2);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        int x = pt.x + choice.getWidth() / 2;
        int y = pt.y + choice.getHeight() / 2 + 70;
        robot.mouseMove(x, y);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        robot.waitForIdle();
        if (choice.getSelectedIndex() == 0) {
            throw new RuntimeException();
        }
    }
}
