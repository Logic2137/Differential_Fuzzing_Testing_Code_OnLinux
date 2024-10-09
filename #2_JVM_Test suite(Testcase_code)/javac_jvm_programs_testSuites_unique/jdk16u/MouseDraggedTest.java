

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MouseDraggedTest {

    private static JFrame frame;
    private static Rectangle frameBounds;
    private static volatile boolean mouseDragged;

    public static void main(String[] args) throws Exception {
        try {
            Robot robot = new Robot();
            robot.setAutoDelay(50);

            SwingUtilities.invokeAndWait(MouseDraggedTest::createAndShowGUI);
            robot.waitForIdle();

            SwingUtilities.invokeAndWait(() -> frameBounds = frame.getBounds());
            robot.waitForIdle();

            for (int i = 1; i <= MouseInfo.getNumberOfButtons(); i++) {
                testMouseDrag(i, robot);
            }
        } finally {
            SwingUtilities.invokeLater(() -> {
                if (frame != null) {
                    frame.dispose();
                }
            });
        }
    }

    private static void testMouseDrag(int button, Robot robot) {

        mouseDragged = false;
        int x1 = frameBounds.x + frameBounds.width / 4;
        int y1 = frameBounds.y + frameBounds.height / 4;
        int x2 = frameBounds.x + frameBounds.width / 2;
        int y2 = frameBounds.y + frameBounds.height / 2;

        robot.mouseMove(x1, y1);
        robot.waitForIdle();

        int buttonMask = InputEvent.getMaskForButton(button);
        robot.mousePress(buttonMask);
        robot.mouseMove(x2, y2);
        robot.mouseRelease(buttonMask);
        robot.waitForIdle();

        if (!mouseDragged) {
            throw new RuntimeException("Mouse button " + button
                    + " is not dragged");
        }
    }

    static void createAndShowGUI() {

        frame = new JFrame();
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel(new BorderLayout());
        panel.addMouseMotionListener(new MouseAdapter() {

            @Override
            public void mouseDragged(MouseEvent e) {
                mouseDragged = true;
            }
        });
        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}
