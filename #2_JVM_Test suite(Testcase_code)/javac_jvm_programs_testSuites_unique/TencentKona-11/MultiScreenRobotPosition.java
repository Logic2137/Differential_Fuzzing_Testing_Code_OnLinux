

import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class MultiScreenRobotPosition {

    private static volatile boolean fail = true;

    public static void main(String[] args) throws Exception {
        GraphicsDevice[] sds = GraphicsEnvironment.getLocalGraphicsEnvironment()
                                                  .getScreenDevices();
        for (final GraphicsDevice gd : sds) {
            fail = true;
            Robot robot = new Robot(gd);
            robot.setAutoDelay(100);
            robot.setAutoWaitForIdle(true);

            Frame frame = new Frame(gd.getDefaultConfiguration());
            frame.setUndecorated(true);
            frame.setSize(400, 400);
            frame.setVisible(true);
            robot.waitForIdle();

            frame.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    System.out.println("e = " + e);
                    fail = false;
                }
            });

            Rectangle bounds = frame.getBounds();
            robot.mouseMove(bounds.x + bounds.width / 2,
                            bounds.y + bounds.height / 2);
            robot.mousePress(MouseEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(MouseEvent.BUTTON1_DOWN_MASK);
            frame.dispose();
            if (fail) {
                System.err.println("Frame bounds = " + bounds);
                throw new RuntimeException("Click in the wrong location");
            }
        }
    }
}
