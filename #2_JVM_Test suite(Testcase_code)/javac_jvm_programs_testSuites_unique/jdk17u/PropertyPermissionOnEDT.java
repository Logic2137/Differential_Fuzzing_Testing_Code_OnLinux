import java.awt.Point;
import java.awt.Robot;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public final class PropertyPermissionOnEDT {

    public static void main(final String[] args) throws Exception {
        SwingUtilities.invokeAndWait(PropertyPermissionOnEDT::test);
        JFrame frame = new JFrame();
        frame.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(final MouseEvent e) {
                test();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                test();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                test();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                test();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                test();
            }
        });
        frame.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                test();
            }

            @Override
            public void focusLost(FocusEvent e) {
                test();
            }
        });
        frame.addMouseWheelListener(e -> test());
        frame.addWindowStateListener(e -> test());
        frame.setSize(100, 100);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        Robot robot = new Robot();
        robot.setAutoWaitForIdle(true);
        robot.setAutoDelay(100);
        Point loc = frame.getLocationOnScreen();
        robot.mouseMove(loc.x + frame.getWidth() / 2, loc.y + frame.getHeight() / 2);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        robot.mouseWheel(100);
        frame.dispose();
    }

    private static void test() {
        String property = System.getProperty("os.name");
        System.out.println("property = " + property);
    }
}
