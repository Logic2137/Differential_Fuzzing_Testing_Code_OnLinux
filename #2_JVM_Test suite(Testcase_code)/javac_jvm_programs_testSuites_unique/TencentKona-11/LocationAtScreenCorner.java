

import java.awt.AWTException;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;


public final class LocationAtScreenCorner {

    public static void main(final String[] args) throws Exception {
        test(true);
        test(false);
    }

    private static void test(final boolean undecorated) throws AWTException {
        Robot robot = new Robot();
        Frame frame = new Frame();
        frame.setUndecorated(undecorated);
        frame.setSize(200, 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        robot.waitForIdle();

        GraphicsEnvironment lge =
                GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = lge.getScreenDevices();

        
        
        
        
        for (GraphicsDevice device : devices) {
            Rectangle bounds = device.getDefaultConfiguration().getBounds();
            test(robot, frame, bounds.x, bounds.y);
            test(robot, frame, bounds.width, bounds.y);
            test(robot, frame, bounds.x, bounds.height);
            test(robot, frame, bounds.width, bounds.height);
        }
        frame.dispose();
    }

    private static void test(Robot robot, Frame frame, int x, int y) {
        for (int i = 0; i < 10; ++i) {
            
            frame.setLocation(x, y); 
            int attempt = 0;
            while (true) {
                robot.waitForIdle();
                
                
                
                Point location = frame.getLocation();
                
                Point locationOnScreen = frame.getLocationOnScreen();
                if (location.equals(locationOnScreen)) {
                    break;
                }
                if (attempt++ > 10) {
                    frame.dispose();
                    System.err.println("Location: " + location);
                    System.err.println("Location on screen: " + locationOnScreen);
                    throw new RuntimeException("Wrong location");
                }
            }
        }
    }
}
