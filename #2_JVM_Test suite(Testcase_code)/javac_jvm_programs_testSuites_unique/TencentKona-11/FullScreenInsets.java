

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Robot;
import java.awt.Window;
import java.awt.image.BufferedImage;


public final class FullScreenInsets {

    private static boolean passed = true;
    private static Robot robot = null;

    public static void main(final String[] args) {
        final GraphicsEnvironment ge = GraphicsEnvironment
                .getLocalGraphicsEnvironment();
        final GraphicsDevice[] devices = ge.getScreenDevices();

        final Window wGreen = new Frame();
        wGreen.setBackground(Color.GREEN);
        wGreen.setSize(300, 300);
        wGreen.setVisible(true);
        sleep();
        final Insets iGreen = wGreen.getInsets();
        final Dimension sGreen = wGreen.getSize();

        final Window wRed = new Frame();
        wRed.setBackground(Color.RED);
        wRed.setSize(300, 300);
        wRed.setVisible(true);
        sleep();
        final Insets iRed = wGreen.getInsets();
        final Dimension sRed = wGreen.getSize();

        for (final GraphicsDevice device : devices) {
            if (!device.isFullScreenSupported()) {
                continue;
            }
            device.setFullScreenWindow(wGreen);
            sleep();
            testWindowBounds(device.getDisplayMode(), wGreen);
            testColor(wGreen, Color.GREEN);

            device.setFullScreenWindow(wRed);
            sleep();
            testWindowBounds(device.getDisplayMode(), wRed);
            testColor(wRed, Color.RED);

            device.setFullScreenWindow(null);
            sleep();
            testInsets(wGreen.getInsets(), iGreen);
            testInsets(wRed.getInsets(), iRed);
            testSize(wGreen.getSize(), sGreen);
            testSize(wRed.getSize(), sRed);
        }
        wGreen.dispose();
        wRed.dispose();
        if (!passed) {
            throw new RuntimeException("Test failed");
        }
    }

    private static void testSize(final Dimension actual, final Dimension exp) {
        if (!exp.equals(actual)) {
            System.err.println(" Wrong window size:" +
                               " Expected: " + exp + " Actual: " + actual);
            passed = false;
        }
    }

    private static void testInsets(final Insets actual, final Insets exp) {
        if (!actual.equals(exp)) {
            System.err.println(" Wrong window insets:" +
                               " Expected: " + exp + " Actual: " + actual);
            passed = false;
        }
    }

    private static void testWindowBounds(final DisplayMode dm, final Window w) {
        if (w.getWidth() != dm.getWidth() || w.getHeight() != dm.getHeight()) {
            System.err.println(" Wrong window bounds:" +
                               " Expected: width = " + dm.getWidth()
                               + ", height = " + dm.getHeight() + " Actual: "
                               + w.getSize());
            passed = false;
        }
    }

    private static void testColor(final Window w, final Color color) {
        final Robot r;
        try {
            r = new Robot(w.getGraphicsConfiguration().getDevice());
        } catch (AWTException e) {
            e.printStackTrace();
            passed = false;
            return;
        }
        final BufferedImage bi = r.createScreenCapture(w.getBounds());
        for (int y = 0; y < bi.getHeight(); y++) {
            for (int x = 0; x < bi.getWidth(); x++) {
                if (bi.getRGB(x, y) != color.getRGB()) {
                    System.err.println(
                            "Incorrect pixel at " + x + "x" + y + " : " +
                            Integer.toHexString(bi.getRGB(x, y)) +
                            " ,expected : " + Integer.toHexString(
                                    color.getRGB()));
                    passed = false;
                    return;
                }
            }
        }
    }

    private static void sleep() {
        if(robot == null) {
            try {
                robot = new Robot();
            }catch(AWTException ae) {
                ae.printStackTrace();
                throw new RuntimeException("Cannot create Robot.");
            }
        }
        robot.waitForIdle();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {
        }
    }
}
