import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.SwingUtilities;

public class JInternalFrameDraggingTest {

    private static JFrame frame;

    private static JDesktopPane desktopPane;

    private static JInternalFrame internalFrame;

    private static int FRAME_SIZE = 500;

    private static Color BACKGROUND_COLOR = Color.ORANGE;

    public static void main(String[] args) throws Exception {
        Robot robot = new Robot();
        robot.setAutoDelay(20);
        SwingUtilities.invokeAndWait(JInternalFrameDraggingTest::createAndShowGUI);
        robot.waitForIdle();
        final int translate = FRAME_SIZE / 4;
        moveFrame(robot, translate, translate / 2, translate / 2);
        robot.waitForIdle();
        Point p = getDesktopPaneLocation();
        int size = translate / 2;
        Rectangle rect = new Rectangle(p.x, p.y, size, size);
        BufferedImage img = robot.createScreenCapture(rect);
        int testRGB = BACKGROUND_COLOR.getRGB();
        for (int i = 1; i < size; i++) {
            int rgbCW = img.getRGB(i, size / 2);
            int rgbCH = img.getRGB(size / 2, i);
            if (rgbCW != testRGB || rgbCH != testRGB) {
                System.out.println("i " + i + " rgbCW " + Integer.toHexString(rgbCW) + " testRGB " + Integer.toHexString(testRGB) + " rgbCH " + Integer.toHexString(rgbCH));
                throw new RuntimeException("Background color is wrong!");
            }
        }
    }

    private static void createAndShowGUI() {
        frame = new JFrame();
        frame.setUndecorated(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        desktopPane = new JDesktopPane();
        desktopPane.setBackground(BACKGROUND_COLOR);
        frame.add(desktopPane, BorderLayout.CENTER);
        frame.setSize(FRAME_SIZE, FRAME_SIZE);
        frame.setVisible(true);
        internalFrame = new JInternalFrame("Test");
        internalFrame.setSize(FRAME_SIZE / 2, FRAME_SIZE / 2);
        desktopPane.add(internalFrame);
        internalFrame.setVisible(true);
        internalFrame.setResizable(true);
        frame.setVisible(true);
    }

    private static void moveFrame(Robot robot, int w, int h, int N) throws Exception {
        Point p = getInternalFrameLocation();
        int xs = p.x + 100;
        int ys = p.y + 15;
        robot.mouseMove(xs, ys);
        try {
            robot.mousePress(InputEvent.BUTTON1_MASK);
            int dx = w / N;
            int dy = h / N;
            int y = ys;
            for (int x = xs; x < xs + w; x += dx, y += dy) {
                robot.mouseMove(x, y);
            }
        } finally {
            robot.mouseRelease(InputEvent.BUTTON1_MASK);
        }
    }

    private static Point getInternalFrameLocation() throws Exception {
        final Point[] points = new Point[1];
        SwingUtilities.invokeAndWait(() -> {
            points[0] = internalFrame.getLocationOnScreen();
        });
        return points[0];
    }

    private static Point getDesktopPaneLocation() throws Exception {
        final Point[] points = new Point[1];
        SwingUtilities.invokeAndWait(() -> {
            points[0] = desktopPane.getLocationOnScreen();
        });
        return points[0];
    }
}
