

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.Window;


public final class SlowMotion {

    
    private static final int SAFE = 100;
    private static final int HEIGHT = 350;
    private static final int WIDTH = 279;
    private static Robot robot;

    public static void main(final String[] args) throws Exception {
        robot = new Robot();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] sds = ge.getScreenDevices();

        for (GraphicsDevice sd : sds) {
            GraphicsConfiguration gc = sd.getDefaultConfiguration();
            Rectangle bounds = gc.getBounds();
            bounds.translate(SAFE, SAFE);
            Point point = new Point(bounds.x, bounds.y);
            Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(gc);
            while (point.y < bounds.y + bounds.height - insets.bottom - HEIGHT - SAFE * 2) {
                while (point.x < bounds.x + bounds.width - insets.right - WIDTH - SAFE * 2) {
                    test(point, new Frame());
                    test(point, new Window(null));
                    test(point, new Dialog((Dialog) null));
                    point.translate(bounds.width / 6, 0);
                }
                point.setLocation(bounds.x, point.y + bounds.height / 5);
            }
        }
    }

    private static void test(final Point loc, Window window) {
        try {
            window.setBounds(loc.x, loc.y, WIDTH, HEIGHT);
            window.setVisible(true);
            robot.delay(1000); 
                               
            Rectangle bounds = window.getBounds();
            if (loc.x != bounds.x || loc.y != bounds.y
                    || bounds.width != WIDTH || bounds.height != HEIGHT) {
                System.err.println("Component = " + window);
                System.err.println("Actual bounds = " + bounds);
                System.err.println("Expected location = " + loc);
                System.err.println("Expected width = " + WIDTH);
                System.err.println("Expected height = " + HEIGHT);
                throw new RuntimeException();
            }
        } finally {
            window.dispose();
        }
    }
}
