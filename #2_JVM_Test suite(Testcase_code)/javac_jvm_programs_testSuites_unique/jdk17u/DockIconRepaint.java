import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.beans.PropertyVetoException;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

public final class DockIconRepaint {

    private static volatile Color color;

    private static JFrame frame;

    private static JInternalFrame jif;

    private static Robot robot;

    private static Point iconLoc;

    private static Rectangle iconBounds;

    public static void main(final String[] args) throws Exception {
        robot = new Robot();
        EventQueue.invokeAndWait(DockIconRepaint::createUI);
        try {
            robot.waitForIdle();
            color = Color.BLUE;
            test();
            color = Color.RED;
            test();
            color = Color.GREEN;
            test();
        } finally {
            frame.dispose();
        }
    }

    private static void test() throws Exception {
        EventQueue.invokeAndWait(() -> {
            try {
                jif.setIcon(false);
                jif.setMaximum(true);
            } catch (PropertyVetoException e) {
                throw new RuntimeException(e);
            }
        });
        robot.waitForIdle();
        Thread.sleep(1000);
        EventQueue.invokeAndWait(() -> {
            try {
                jif.setIcon(true);
            } catch (PropertyVetoException e) {
                throw new RuntimeException(e);
            }
            iconLoc = jif.getDesktopIcon().getLocationOnScreen();
            iconBounds = jif.getDesktopIcon().getBounds();
        });
        robot.waitForIdle();
        Thread.sleep(1000);
        final Color c = robot.getPixelColor(iconLoc.x + iconBounds.width / 2, iconLoc.y + iconBounds.height / 2);
        if (c.getRGB() != color.getRGB()) {
            System.err.println("Exp: " + Integer.toHexString(color.getRGB()));
            System.err.println("Actual: " + Integer.toHexString(c.getRGB()));
            throw new RuntimeException("Wrong color.");
        }
    }

    private static void createUI() {
        frame = new JFrame();
        frame.setUndecorated(true);
        frame.setSize(300, 300);
        frame.setLocationRelativeTo(null);
        final JDesktopPane pane = new JDesktopPane();
        final JPanel panel = new JPanel() {

            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(color);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        jif = new JInternalFrame();
        jif.add(panel);
        jif.setVisible(true);
        jif.setSize(300, 300);
        pane.add(jif);
        frame.add(pane);
        frame.setVisible(true);
    }
}
