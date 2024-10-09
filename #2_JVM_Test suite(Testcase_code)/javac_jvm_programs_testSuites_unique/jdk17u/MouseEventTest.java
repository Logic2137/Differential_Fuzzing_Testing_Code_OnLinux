import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseEventTest {

    static volatile boolean crossed = false;

    static void sleep(Robot robot) throws InterruptedException {
        robot.waitForIdle();
        Thread.sleep(500);
    }

    public static void main(String[] args) throws AWTException, InterruptedException {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gds = ge.getScreenDevices();
        if (gds.length < 2) {
            System.out.println("It's a multiscreen test... skipping!");
            return;
        }
        for (int i = 0; i < gds.length; ++i) {
            GraphicsDevice gd = gds[i];
            GraphicsConfiguration gc = gd.getDefaultConfiguration();
            Rectangle screen = gc.getBounds();
            Robot robot = new Robot(gd);
            robot.setAutoDelay(100);
            Frame frame = new Frame(gc);
            frame.setUndecorated(true);
            frame.setSize(200, 200);
            frame.setLocation(screen.x + 200, screen.y + 200);
            frame.setBackground(Color.YELLOW);
            frame.setVisible(true);
            sleep(robot);
            Point loc = frame.getLocationOnScreen();
            Dimension size = frame.getSize();
            final Point point = new Point(loc.x + size.width / 2, loc.y + size.height / 2);
            crossed = false;
            frame.addMouseMotionListener(new MouseAdapter() {

                @Override
                public void mouseMoved(MouseEvent e) {
                    if (point.equals(e.getLocationOnScreen())) {
                        crossed = true;
                    }
                }
            });
            robot.mouseMove(point.x - 1, point.y - 1);
            robot.mouseMove(point.x, point.y);
            sleep(robot);
            frame.dispose();
            if (!crossed) {
                throw new RuntimeException("An expected mouse motion event was not received on the screen #" + i);
            }
        }
        System.out.println("Test PASSED!");
    }
}
