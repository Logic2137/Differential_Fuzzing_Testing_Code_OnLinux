

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;


public class DNDTextToScaledArea {

    private static final int SIZE = 300;
    private static final String TEXT = "ABCDEFGH";
    private static JFrame frame;
    private static JTextArea srcTextArea;
    private static JTextArea dstTextArea;
    private static volatile Point srcPoint;
    private static volatile Point dstPoint;
    private static volatile boolean passed = false;

    public static void main(String[] args) throws Exception {
        var lge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        for (GraphicsDevice device : lge.getScreenDevices()) {
            test(device);
        }
    }

    private static void test(GraphicsDevice device) throws Exception {
        Robot robot = new Robot();
        robot.setAutoDelay(150);

        SwingUtilities.invokeAndWait(() -> createAndShowGUI(device));
        robot.waitForIdle();

        SwingUtilities.invokeAndWait(() -> {
            srcPoint = getPoint(srcTextArea, 0.1);
            dstPoint = getPoint(dstTextArea, 0.75);
        });
        robot.waitForIdle();
        
        robot.mouseMove(dstPoint.x, dstPoint.y);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        robot.waitForIdle();

        dragAndDrop(robot, srcPoint, dstPoint);
        robot.waitForIdle();

        SwingUtilities.invokeAndWait(() -> {
            passed = TEXT.equals(dstTextArea.getText());
            frame.dispose();
        });
        robot.waitForIdle();

        if (!passed) {
            throw new RuntimeException("Text Drag and Drop failed!");
        }
    }

    private static void createAndShowGUI(GraphicsDevice device) {
        frame = new JFrame(device.getDefaultConfiguration());
        Rectangle screen = device.getDefaultConfiguration().getBounds();
        int x = (int) (screen.getCenterX() - SIZE / 2);
        int y = (int) (screen.getCenterY() - SIZE / 2);
        frame.setBounds(x, y, SIZE, SIZE);

        JPanel panel = new JPanel(new BorderLayout());

        srcTextArea = new JTextArea(TEXT);
        srcTextArea.setDragEnabled(true);
        srcTextArea.selectAll();
        dstTextArea = new JTextArea();

        panel.add(dstTextArea, BorderLayout.CENTER);
        panel.add(srcTextArea, BorderLayout.SOUTH);

        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }

    private static Point getPoint(Component component, double scale) {
        Point point = component.getLocationOnScreen();
        Dimension bounds = component.getSize();
        point.translate((int) (bounds.width * scale), (int) (bounds.height * scale));
        return point;
    }

    public static void dragAndDrop(Robot robot, Point src, Point dst) throws Exception {

        int x1 = src.x;
        int y1 = src.y;
        int x2 = dst.x;
        int y2 = dst.y;
        robot.mouseMove(x1, y1);
        robot.mousePress(InputEvent.BUTTON1_MASK);

        float dmax = (float) Math.max(Math.abs(x2 - x1), Math.abs(y2 - y1));
        float dx = (x2 - x1) / dmax;
        float dy = (y2 - y1) / dmax;

        for (int i = 0; i <= dmax; i += 5) {
            robot.mouseMove((int) (x1 + dx * i), (int) (y1 + dy * i));
        }

        robot.mouseRelease(InputEvent.BUTTON1_MASK);
    }
}
