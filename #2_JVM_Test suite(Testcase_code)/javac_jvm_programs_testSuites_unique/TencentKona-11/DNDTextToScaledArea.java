

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;


public class DNDTextToScaledArea {

    private static final String TEXT = "ABCDEFGH";
    private static JFrame frame;
    private static JTextArea srcTextArea;
    private static JTextArea dstTextArea;
    private static volatile Point srcPoint;
    private static volatile Point dstPoint;
    private static volatile boolean passed = false;

    public static void main(String[] args) throws Exception {
        Robot robot = new Robot();
        robot.setAutoDelay(50);

        SwingUtilities.invokeAndWait(DNDTextToScaledArea::createAndShowGUI);
        robot.waitForIdle();

        SwingUtilities.invokeAndWait(() -> {
            srcPoint = getPoint(srcTextArea, 0.1);
            dstPoint = getPoint(dstTextArea, 0.75);
        });
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

    private static void createAndShowGUI() {

        frame = new JFrame();
        frame.setSize(300, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
