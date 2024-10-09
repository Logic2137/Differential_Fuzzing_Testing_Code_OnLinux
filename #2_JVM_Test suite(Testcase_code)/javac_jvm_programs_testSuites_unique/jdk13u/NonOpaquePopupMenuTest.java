import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class NonOpaquePopupMenuTest extends JFrame {

    private static JMenu fileMenu;

    public NonOpaquePopupMenuTest() {
        getContentPane().setBackground(java.awt.Color.RED);
        JMenuBar menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        JMenuItem menuItem = new JMenuItem("New");
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
        fileMenu.add(menuItem);
        fileMenu.getPopupMenu().setOpaque(false);
        setSize(new Dimension(640, 480));
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) throws Throwable {
        Robot robot = new Robot();
        robot.setAutoDelay(250);
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                new NonOpaquePopupMenuTest();
            }
        });
        robot.waitForIdle();
        Point p = getMenuClickPoint();
        robot.mouseMove(p.x, p.y);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        robot.waitForIdle();
        if (isParentOpaque()) {
            throw new RuntimeException("Popup menu parent is opaque");
        }
    }

    private static boolean isParentOpaque() throws Exception {
        final boolean[] result = new boolean[1];
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                result[0] = fileMenu.getPopupMenu().getParent().isOpaque();
            }
        });
        return result[0];
    }

    private static Point getMenuClickPoint() throws Exception {
        final Point[] result = new Point[1];
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                Point p = fileMenu.getLocationOnScreen();
                Dimension size = fileMenu.getSize();
                result[0] = new Point(p.x + size.width / 2, p.y + size.height / 2);
            }
        });
        return result[0];
    }
}
