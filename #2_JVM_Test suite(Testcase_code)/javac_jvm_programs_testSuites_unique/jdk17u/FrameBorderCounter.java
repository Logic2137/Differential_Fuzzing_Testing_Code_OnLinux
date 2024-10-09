import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FrameBorderCounter {

    private static Frame frame;

    private static Frame background;

    private static Dimension size;

    private static Point location;

    private static Point entered;

    public static void main(String[] args) throws Exception {
        final Robot robot = new Robot();
        EventQueue.invokeAndWait(new Runnable() {

            public void run() {
                robot.mouseMove(0, 0);
            }
        });
        EventQueue.invokeAndWait(new Runnable() {

            public void run() {
                background = new Frame();
                background.setBounds(100, 100, 300, 300);
                background.addMouseListener(new MouseAdapter() {

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        entered = e.getLocationOnScreen();
                        System.err.println("[ENTERED] : " + entered);
                    }
                });
                background.setVisible(true);
            }
        });
        robot.waitForIdle();
        EventQueue.invokeAndWait(new Runnable() {

            public void run() {
                frame = new Frame("Frame");
                frame.setBounds(200, 200, 100, 100);
                frame.setVisible(true);
            }
        });
        Thread.sleep(1000);
        EventQueue.invokeAndWait(new Runnable() {

            public void run() {
                location = frame.getLocationOnScreen();
                size = frame.getSize();
            }
        });
        int out = 20;
        for (int x = location.x + size.width - out; x <= location.x + size.width + out; ++x) {
            robot.mouseMove(x, location.y + size.height / 2);
            Thread.sleep(50);
        }
        System.err.println("[LOCATION] : " + location);
        System.err.println("[SIZE] : " + size);
        Thread.sleep(250);
        int shift = entered.x - location.x - size.width - 1;
        System.err.println("Done");
        System.out.println(shift);
        frame.dispose();
        background.dispose();
    }
}
