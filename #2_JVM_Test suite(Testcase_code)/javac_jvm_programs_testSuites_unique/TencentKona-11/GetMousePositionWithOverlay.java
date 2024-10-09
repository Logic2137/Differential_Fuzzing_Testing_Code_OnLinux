

import java.awt.Frame;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Rectangle;



public class GetMousePositionWithOverlay {

    private static Frame backFrame;
    private static Frame frontFrame;
    private static Robot robot;

    public static void main(String[] args) throws Throwable {
        robot = new Robot();

        try{
            constructTestUI();
        } catch (Exception e) {
            dispose();
            throw new RuntimeException("Unexpected Exception!");
        }

        robot.waitForIdle();

        doTest();
        dispose();
    }

    private static void doTest() {

        frontFrame.toFront();
        robot.waitForIdle();

        Rectangle bounds = new Rectangle(frontFrame.getLocationOnScreen(), frontFrame.getSize());
        robot.mouseMove(bounds.x + bounds.width / 2, bounds.y + bounds.height / 2);
        robot.waitForIdle();

        Point pos = backFrame.getMousePosition();
        if (pos != null) {
            dispose();
            throw new RuntimeException("Test failed. Mouse position should be null but was " + pos);
        }

        pos = frontFrame.getMousePosition();
        if (pos == null) {
            dispose();
            throw new RuntimeException("Test failed. Mouse position should not be null");
        }

        robot.mouseMove(189, 189);
        robot.waitForIdle();

        pos = backFrame.getMousePosition();
        if (pos == null) {
            dispose();
            throw new RuntimeException("Test failed. Mouse position should not be null");
        }

    }

    private static void dispose() {

        if (backFrame != null) {
            backFrame.dispose();
        }

        if (frontFrame != null) {
            frontFrame.dispose();
        }
    }

    private static void constructTestUI() {
        backFrame = new Frame();
        backFrame.setBounds(100, 100, 100, 100);
        backFrame.setResizable(false);
        backFrame.setVisible(true);

        frontFrame = new Frame();
        frontFrame.setBounds(120, 120, 60, 60);
        frontFrame.setResizable(false);
        frontFrame.setVisible(true);

    }
}

