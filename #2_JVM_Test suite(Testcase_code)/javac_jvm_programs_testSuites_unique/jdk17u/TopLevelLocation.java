import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JWindow;

public class TopLevelLocation {

    private static JFrame frame;

    private static JWindow window;

    private static boolean passed = true;

    public static void main(String[] args) throws Exception {
        EventQueue.invokeAndWait(() -> {
            frame = new JFrame();
            frame.getContentPane().setBackground(Color.PINK);
            frame.setBounds(100, 100, 500, 400);
            frame.setUndecorated(true);
            frame.setVisible(true);
            window = new JWindow(frame);
            window.setBackground(Color.BLUE);
            window.setAlwaysOnTop(true);
            window.setBounds(200, 200, 200, 200);
            window.addMouseListener(new MouseAdapter() {

                private Point dragOrigin = null;

                private Dimension origSize = null;

                private Point origLoc = null;

                private Point lastLoc = null;

                private boolean left = false;

                private boolean top = false;

                private boolean bottom = false;

                private boolean right = false;

                @Override
                public void mousePressed(MouseEvent e) {
                    System.out.println("mousePressed");
                    dragOrigin = e.getLocationOnScreen();
                    origSize = window.getSize();
                    origLoc = window.getLocationOnScreen();
                    if (lastLoc != null) {
                        System.out.println("SET LOCATION: " + lastLoc);
                        System.out.println("CURRENT LOCATION: " + origLoc);
                        if (lastLoc.x != origLoc.x || lastLoc.y != origLoc.y) {
                            passed = false;
                        }
                    }
                    right = (origLoc.x + window.getWidth() - dragOrigin.x) < 5;
                    left = !right && dragOrigin.x - origLoc.x < 5;
                    bottom = (origLoc.y + window.getHeight() - dragOrigin.y) < 5;
                    top = !bottom && dragOrigin.y - origLoc.y < 5;
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    System.out.println("mouseDragged");
                    resize(e);
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    System.out.println("mouseReleased");
                    resize(e);
                }

                void resize(MouseEvent e) {
                    Point dragDelta = e.getLocationOnScreen();
                    dragDelta.translate(-dragOrigin.x, -dragOrigin.y);
                    Point newLoc = new Point(origLoc);
                    newLoc.translate(dragDelta.x, dragDelta.y);
                    Dimension newSize = new Dimension(origSize);
                    if (left || right) {
                        newSize.width += right ? dragDelta.x : -dragDelta.x;
                    }
                    if (top || bottom) {
                        newSize.height += bottom ? dragDelta.y : -dragDelta.y;
                    }
                    if (right || (top || bottom) && !left) {
                        newLoc.x = origLoc.x;
                    }
                    if (bottom || (left || right) && !top) {
                        newLoc.y = origLoc.y;
                    }
                    window.setBounds(newLoc.x, newLoc.y, newSize.width, newSize.height);
                    lastLoc = newLoc;
                }
            });
            window.setVisible(true);
        });
        Thread.sleep(500);
        Dimension size = window.getSize();
        Point location = window.getLocation();
        Robot robot = new Robot();
        robot.setAutoDelay(200);
        robot.setAutoWaitForIdle(true);
        robot.waitForIdle();
        robot.mouseMove(location.x + size.height - 2, location.y + size.width - 2);
        robot.mousePress(MouseEvent.BUTTON1_DOWN_MASK);
        robot.mouseMove(location.x + size.height, location.y + size.width);
        robot.mouseRelease(MouseEvent.BUTTON1_DOWN_MASK);
        robot.mousePress(MouseEvent.BUTTON1_DOWN_MASK);
        robot.mouseMove(location.x + size.height + 2, location.y + size.width + 2);
        robot.mouseRelease(MouseEvent.BUTTON1_DOWN_MASK);
        Thread.sleep(500);
        frame.dispose();
        if (!passed) {
            throw new RuntimeException("TEST FAILED: Location doesn't match!");
        }
        System.out.println("TEST PASSED!");
    }
}
