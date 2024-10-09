
package test.java.awt.regtesthelpers;

import java.awt.*;
import java.awt.event.*;

public class VisibilityValidator {

    static final int SETUP_PERIOD = 5000;

    static final boolean DEBUG = false;

    volatile Window win = null;

    boolean activated = false;

    boolean opened = false;

    boolean focused = false;

    volatile boolean valid = false;

    public static void setVisibleAndConfirm(Frame testframe) throws Exception {
        setVisibleAndConfirm(testframe, "Could not confirm test frame was " + "visible");
    }

    public static void setVisibleAndConfirm(Frame testframe, String msg) throws Exception {
        if (testframe.isVisible()) {
            throw new RuntimeException("Frame is already visible");
        }
        VisibilityValidator checkpoint = new VisibilityValidator(testframe);
        testframe.setVisible(true);
        checkpoint.requireVisible();
        if (!checkpoint.isValid()) {
            throw new Exception("Frame not visible after " + SETUP_PERIOD + " milliseconds");
        }
    }

    public VisibilityValidator(Window win) {
        this.win = win;
        WindowAdapter watcher = new WindowAdapter() {

            public void windowOpened(WindowEvent e) {
                doOpen();
            }

            public void windowActivated(WindowEvent e) {
                doActivate();
            }

            public void windowGainedFocus(WindowEvent e) {
                doGainedFocus();
            }
        };
        win.addWindowListener(watcher);
        win.addWindowFocusListener(watcher);
    }

    synchronized public void requireVisible() {
        int tries = 0;
        try {
            while ((opened == false) || (activated == false) || (focused == false)) {
                if (tries < 4) {
                    tries += 1;
                    wait(SETUP_PERIOD);
                } else {
                    break;
                }
            }
            if (opened && activated) {
                valid = true;
            } else {
                valid = false;
            }
        } catch (InterruptedException ix) {
            valid = false;
        }
        if (win.isVisible() == false) {
            valid = false;
        }
        if (win.isShowing() == false) {
            valid = false;
        }
        if (win.isFocused() == false) {
            valid = false;
        }
        if (DEBUG) {
            if (!isValid()) {
                System.out.println("\tactivated:" + new Boolean(activated));
                System.out.println("\topened:" + new Boolean(opened));
                System.out.println("\tfocused:" + new Boolean(focused));
                System.out.println("\tvalid:" + new Boolean(valid));
                System.out.println("\tisVisible():" + new Boolean(win.isVisible()));
                System.out.println("\tisShowing():" + new Boolean(win.isShowing()));
                System.out.println("\tisFocused():" + new Boolean(win.isFocused()));
            }
        }
    }

    synchronized void doOpen() {
        opened = true;
        notify();
    }

    synchronized void doActivate() {
        activated = true;
        notify();
    }

    synchronized void doGainedFocus() {
        focused = true;
        notify();
    }

    public boolean isValid() {
        return valid;
    }

    public boolean isClear() {
        return valid;
    }

    volatile static Robot robot = null;

    public static boolean waitForColor(Component c, Color expected) throws AWTException, InterruptedException {
        Dimension dim = c.getSize();
        int xOff = dim.width / 2;
        int yOff = dim.height / 2;
        return waitForColor(c, xOff, yOff, expected);
    }

    public static boolean waitForColor(Component c, int xOff, int yOff, Color expected) throws AWTException, InterruptedException {
        return waitForColor(c, xOff, yOff, expected, 5000L);
    }

    public static boolean waitForColor(Component c, int xOff, int yOff, Color expected, long timeout) throws AWTException, InterruptedException {
        Point p = c.getLocationOnScreen();
        int x = (int) p.getX() + xOff;
        int y = (int) p.getY() + yOff;
        return waitForColor(x, y, expected, timeout);
    }

    public static boolean waitForColor(int locX, int locY, Color expected, long timeout) throws AWTException, InterruptedException {
        if (robot == null) {
            robot = new Robot();
        }
        long endtime = System.currentTimeMillis() + timeout;
        while (endtime > System.currentTimeMillis()) {
            if (colorMatch(robot.getPixelColor(locX, locY), expected)) {
                return true;
            }
            Thread.sleep(50);
        }
        return false;
    }

    public static void assertColorEquals(final String message, final Color actual, final Color expected) {
        System.out.println("actual color: " + actual);
        System.out.println("expect color: " + expected);
    }

    public static boolean colorMatch(final Color actual, final Color expected) {
        final float[] actualHSB = getHSB(actual);
        final float[] expectedHSB = getHSB(expected);
        final float actualHue = actualHSB[0];
        final float expectedHue = expectedHSB[0];
        final boolean hueMatched = closeMatchHue(actualHue, expectedHue, 0.17f);
        final float actualBrightness = actualHSB[2];
        final float expectedBrightness = expectedHSB[2];
        final boolean brightnessMatched = closeMatch(actualBrightness, expectedBrightness, 0.15f);
        if (brightnessMatched && !hueMatched) {
            return (expectedBrightness < 0.15f);
        }
        return brightnessMatched && hueMatched;
    }

    static float[] getHSB(final Color color) {
        final float[] hsb = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
        return hsb;
    }

    static boolean closeMatchHue(final float actual, final float expected, final float tolerance) {
        if (closeMatch(actual, expected, tolerance)) {
            return true;
        }
        final float expectedHigh = expected + tolerance;
        final float expectedLow = expected - tolerance;
        if (expectedHigh > 1.0f) {
            return closeMatch(actual + 0.5f, expected - 0.5f, tolerance);
        }
        if (expectedLow < 0.0f) {
            return closeMatch(actual - 0.5f, expected + 0.5f, tolerance);
        }
        return false;
    }

    static boolean closeMatch(final float actual, final float expected, final float tolerance) {
        return (expected + tolerance) > actual && (expected - tolerance) < actual;
    }
}
