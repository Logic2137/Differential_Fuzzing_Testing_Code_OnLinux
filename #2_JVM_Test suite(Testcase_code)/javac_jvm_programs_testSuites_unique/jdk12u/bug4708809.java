


import javax.swing.*;
import java.awt.*;
import java.awt.Point;
import java.awt.event.*;

public class bug4708809 {

    private static volatile boolean do_test = false;
    private static volatile boolean passed = true;
    private static JScrollPane spane;
    private static JScrollBar sbar;

    public static void main(String[] args) throws Exception {
        Robot robot = new Robot();
        robot.setAutoDelay(350);

        SwingUtilities.invokeAndWait(new Runnable() {

            public void run() {
                createAndShowGUI();
            }
        });

        robot.waitForIdle();

        SwingUtilities.invokeAndWait(new Runnable() {

            public void run() {
                spane.requestFocus();
                sbar.setValue(sbar.getMaximum());
            }
        });

        robot.waitForIdle();

        Point point = getClickPoint(0.5, 0.5);
        robot.mouseMove(point.x, point.y);
        robot.mousePress(InputEvent.BUTTON1_MASK);

        robot.waitForIdle();

        SwingUtilities.invokeAndWait(new Runnable() {

            public void run() {
                final int oldValue = sbar.getValue();
                sbar.addAdjustmentListener(new AdjustmentListener() {

                    public void adjustmentValueChanged(AdjustmentEvent e) {
                        if (e.getValue() >= oldValue) {
                            passed = false;
                        }
                        do_test = true;
                    }
                });

            }
        });

        robot.waitForIdle();

        point = getClickPoint(0.5, 0.2);
        robot.mouseMove(point.x, point.y);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        robot.waitForIdle();

        if (!do_test || !passed) {
            throw new Exception("The scrollbar moved with incorrect direction");
        }

    }

    private static Point getClickPoint(final double scaleX, final double scaleY) throws Exception {
        final Point[] result = new Point[1];

        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                Point p = sbar.getLocationOnScreen();
                Rectangle rect = sbar.getBounds();
                result[0] = new Point((int) (p.x + scaleX * rect.width),
                        (int) (p.y + scaleY * rect.height));
            }
        });

        return result[0];

    }

    private static void createAndShowGUI() {
        JFrame fr = new JFrame("Test");

        JLabel label = new JLabel("picture");
        label.setPreferredSize(new Dimension(500, 500));
        spane = new JScrollPane(label);
        fr.getContentPane().add(spane);
        sbar = spane.getVerticalScrollBar();

        fr.setSize(200, 200);
        fr.setVisible(true);
    }
}
