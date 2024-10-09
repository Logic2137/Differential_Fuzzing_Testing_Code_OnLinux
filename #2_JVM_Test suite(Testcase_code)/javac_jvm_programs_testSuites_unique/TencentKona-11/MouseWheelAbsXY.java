

import java.awt.AWTException;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Robot;
import java.awt.Window;
import java.awt.event.InputEvent;

import static java.awt.GraphicsEnvironment.*;


public final class MouseWheelAbsXY {

    private static boolean done;
    private static int wheelX;
    private static int wheelY;
    private static int mouseX;
    private static int mouseY;

    public static void main(final String[] args) throws AWTException {
        GraphicsEnvironment ge = getLocalGraphicsEnvironment();
        GraphicsDevice[] sds = ge.getScreenDevices();
        for (GraphicsDevice gd : sds) {
            test(gd.getDefaultConfiguration());
        }
    }

    private static void test(GraphicsConfiguration gc) throws AWTException {
        final Window frame = new Frame(gc);
        try {
            frame.addMouseWheelListener(e -> {
                wheelX = e.getXOnScreen();
                wheelY = e.getYOnScreen();
                done = true;
            });
            frame.setSize(300, 300);
            frame.setVisible(true);

            final Robot robot = new Robot();
            robot.setAutoDelay(50);
            robot.setAutoWaitForIdle(true);
            mouseX = frame.getX() + frame.getWidth() / 2;
            mouseY = frame.getY() + frame.getHeight() / 2;

            robot.mouseMove(mouseX, mouseY);
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseWheel(10);

            validate();
        } finally {
            frame.dispose();
        }
    }

    private static void validate() {
        if (!done || wheelX != mouseX || wheelY != mouseY) {
            System.err.println("Expected X: " + mouseX);
            System.err.println("Expected Y: " + mouseY);
            System.err.println("Actual X: " + wheelX);
            System.err.println("Actual Y: " + wheelY);
            throw new RuntimeException("Test failed");
        }
    }
}
