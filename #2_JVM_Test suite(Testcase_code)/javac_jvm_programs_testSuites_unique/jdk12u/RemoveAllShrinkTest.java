



import java.awt.*;
import java.awt.event.*;


public class RemoveAllShrinkTest {

    public static void main(String[] args) {
        Frame f = new Frame();
        Choice choice = new Choice();

        for (int i = 0; i < 10; ++i) {
            choice.addItem("Item " + i);
        }

        f.add(choice, BorderLayout.NORTH);
        Panel panel = new Panel();
        panel.setBackground(Color.RED);
        f.add(panel);

        f.setSize(200, 200);
        f.setVisible(true);
        f.toFront();

        choice.removeAll();

        try {
            Robot robot = new Robot();
            robot.setAutoWaitForIdle(true);
            robot.setAutoDelay(50);

            robot.waitForIdle();
            Thread.sleep(200);

            Point pt = choice.getLocationOnScreen();
            robot.mouseMove(pt.x + choice.getWidth() - choice.getHeight() / 2,
                    pt.y + choice.getHeight() / 2);
            robot.mousePress(InputEvent.BUTTON1_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_MASK);

            Thread.sleep(400);

            Point pt1 = panel.getLocationOnScreen();

            Color color = robot.getPixelColor(pt1.x + panel.getWidth() / 2,
                    pt1.y + panel.getHeight() / 2);

            if (!color.equals(Color.RED)) {
                throw new RuntimeException("RemoveAllShrinkTest failed. " + color);
            }
        } catch (Exception e) {
            throw new RuntimeException("The test was not completed.\n\n" + e);
        }
    }
}

