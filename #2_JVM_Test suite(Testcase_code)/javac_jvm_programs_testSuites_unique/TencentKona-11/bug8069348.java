import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import static sun.awt.OSInfo.*;

public class bug8069348 {

    private static final int WIN_WIDTH = 500;

    private static final int WIN_HEIGHT = 500;

    private static final Color DESKTOPPANE_COLOR = Color.YELLOW;

    private static final Color FRAME_COLOR = Color.ORANGE;

    private static JFrame frame;

    private static JInternalFrame internalFrame;

    public static void main(String[] args) throws Exception {
        if (!isSupported()) {
            return;
        }
        try {
            SwingUtilities.invokeAndWait(bug8069348::createAndShowGUI);
            Robot robot = new Robot();
            robot.setAutoDelay(50);
            robot.waitForIdle();
            Rectangle screenBounds = getInternalFrameScreenBounds();
            int x = screenBounds.x + screenBounds.width / 2;
            int y = screenBounds.y + 10;
            int dx = screenBounds.width / 2;
            int dy = screenBounds.height / 2;
            robot.mouseMove(x, y);
            robot.waitForIdle();
            robot.mousePress(InputEvent.BUTTON1_MASK);
            robot.mouseMove(x + dx, y + dy);
            robot.mouseRelease(InputEvent.BUTTON1_MASK);
            robot.waitForIdle();
            int cx = screenBounds.x + screenBounds.width + dx / 2;
            int cy = screenBounds.y + screenBounds.height + dy / 2;
            robot.mouseMove(cx, cy);
            if (!FRAME_COLOR.equals(robot.getPixelColor(cx, cy))) {
                throw new RuntimeException("Internal frame is not correctly dragged!");
            }
        } finally {
            if (frame != null) {
                frame.dispose();
            }
        }
    }

    private static boolean isSupported() {
        String d3d = System.getProperty("sun.java2d.d3d");
        return !Boolean.getBoolean(d3d) || getOSType() == OSType.WINDOWS;
    }

    private static Rectangle getInternalFrameScreenBounds() throws Exception {
        Rectangle[] points = new Rectangle[1];
        SwingUtilities.invokeAndWait(() -> {
            points[0] = new Rectangle(internalFrame.getLocationOnScreen(), internalFrame.getSize());
        });
        return points[0];
    }

    private static void createAndShowGUI() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JDesktopPane desktopPane = new JDesktopPane();
        desktopPane.setBackground(DESKTOPPANE_COLOR);
        internalFrame = new JInternalFrame("Test") {

            @Override
            public void paint(Graphics g) {
                super.paint(g);
                g.setColor(FRAME_COLOR);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        internalFrame.setSize(WIN_WIDTH / 3, WIN_HEIGHT / 3);
        internalFrame.setVisible(true);
        desktopPane.add(internalFrame);
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(desktopPane, BorderLayout.CENTER);
        frame.add(panel);
        frame.setSize(WIN_WIDTH, WIN_HEIGHT);
        frame.setVisible(true);
        frame.requestFocus();
    }
}
