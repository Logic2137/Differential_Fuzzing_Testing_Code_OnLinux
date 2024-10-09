

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;


public final class CheckCommonColors {

    private static final Frame frame = new Frame();
    private static Robot robot;

    public static void main(final String[] args) throws Exception {
        robot = new Robot();
        var ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        for (GraphicsDevice gd : ge.getScreenDevices()) {
            try {
                test(gd.getDefaultConfiguration().getBounds());
            } finally {
                frame.dispose();
            }
        }
    }

    private static void test(Rectangle screen) {
        frame.setSize(400, 400);
        frame.setLocation((int)screen.getCenterX() - 200,
                          (int)screen.getCenterY() - 200);
        frame.setUndecorated(true);
        for (final Color color : List.of(Color.WHITE, Color.LIGHT_GRAY,
                                         Color.GRAY, Color.DARK_GRAY,
                                         Color.BLACK, Color.RED, Color.PINK,
                                         Color.ORANGE, Color.YELLOW,
                                         Color.GREEN, Color.MAGENTA, Color.CYAN,
                                         Color.BLUE)) {
            frame.dispose();
            robot.waitForIdle();
            frame.setBackground(color);
            frame.setVisible(true);
            checkPixels(color, true);
            checkPixels(color, false);
        }
    }

    private static void checkPixels(final Color color, boolean useRect) {
        System.out.println("color = " + color + ", useRect = " + useRect);
        int attempt = 0;
        while (true) {
            Point p = frame.getLocationOnScreen();
            p.translate(frame.getWidth() / 2, frame.getHeight() / 2);
            Color pixel;
            Rectangle rect = new Rectangle(p.x, p.y, 1, 1);
            if (useRect) {
                BufferedImage bi = robot.createScreenCapture(rect);
                pixel = new Color(bi.getRGB(0, 0));
            } else {
                pixel = robot.getPixelColor(rect.x, rect.y);
            }
            if (color.equals(pixel)) {
                return;
            }
            frame.repaint();
            if (attempt > 11) {
                System.err.println("Expected: " + color);
                System.err.println("Actual: " + pixel);
                System.err.println("Point: " + p);
                Dimension screenSize =
                        Toolkit.getDefaultToolkit().getScreenSize();
                BufferedImage screen = robot.createScreenCapture(
                        new Rectangle(screenSize));
                try {
                    File output = new File("ScreenCapture.png");
                    System.err.println("Dump screen to: " + output);
                    ImageIO.write(screen, "png", output);
                } catch (IOException ex) {}
                throw new RuntimeException("Too many attempts: " + attempt);
            }
            
            
            robot.delay((int) Math.pow(2.2, attempt++));
        }
    }
}
