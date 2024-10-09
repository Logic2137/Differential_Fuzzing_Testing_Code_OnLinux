


import java.awt.AWTException;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.SwingUtilities;

public class JRobot extends java.awt.Robot {
    private static int DEFAULT_DELAY = 550;
    private static int INTERNAL_DELAY = 250;

    private int delay;
    private boolean delaysEnabled;

    protected JRobot(boolean enableDelays) throws AWTException {
        super();
        delaysEnabled = enableDelays;
        setAutoWaitForIdle(enableDelays);
        if (enableDelays) {
            setAutoDelay(INTERNAL_DELAY);
            setDelay(DEFAULT_DELAY);
        }
    }

    
    public static JRobot getRobot() {
        return getRobot(true);
    }

    
    public static JRobot getRobot(boolean enableDelays) {
        JRobot robot = null;
        try {
            robot = new JRobot(enableDelays);
        } catch (AWTException e) {
            System.err.println("Coudn't create Robot, details below");
            throw new Error(e);
        }
        return robot;
    }

    
    public void hitKey(int keycode) {
        keyPress(keycode);
        keyRelease(keycode);
        delay();
    }

    
    public void hitKey(int... keys) {
        for (int i = 0; i < keys.length; i++) {
            keyPress(keys[i]);
        }

        for (int i = keys.length - 1; i >= 0; i--) {
            keyRelease(keys[i]);
        }
        delay();
    }

    
    public void moveMouseTo(Component c) {
        Point p = c.getLocationOnScreen();
        Dimension size = c.getSize();
        p.x += size.width / 2;
        p.y += size.height / 2;
        mouseMove(p.x, p.y);
        delay();
    }

    
    public void glide(int x0, int y0, int x1, int y1) {
        float dmax = (float)Math.max(Math.abs(x1 - x0), Math.abs(y1 - y0));
        float dx = (x1 - x0) / dmax;
        float dy = (y1 - y0) / dmax;

        mouseMove(x0, y0);
        for (int i=1; i<=dmax; i++) {
            mouseMove((int)(x0 + dx*i), (int)(y0 + dy*i));
        }
        delay();
    }

    
    public void clickMouse(int buttons) {
        mousePress(buttons);
        mouseRelease(buttons);
        delay();
    }

    
    public void clickMouse() {
        clickMouse(InputEvent.BUTTON1_MASK);
    }

    
    public void clickMouseOn(Component c, int buttons) {
        moveMouseTo(c);
        clickMouse(buttons);
    }

    
    public void clickMouseOn(Component c) {
        clickMouseOn(c, InputEvent.BUTTON1_MASK);
    }

    
    public boolean getDelaysEnabled() {
        return delaysEnabled;
    }

    
    public void delay() {
        delay(delay);
    }

    
    public int getDelay() {
        return delay;
    }

    
    public void setDelay(int delay) {
        this.delay = delay;
    }

    
    public synchronized void waitForIdle() {
        if (!EventQueue.isDispatchThread()) {
            super.waitForIdle();
        }
    }

    
    public Point centerOf(Rectangle r) {
        return new Point(r.x + r.width / 2, r.y + r.height / 2);
    }

    
    public Point centerOf(Rectangle r, Point p) {
        p.x = r.x + r.width / 2;
        p.y = r.y + r.height / 2;
        return p;
    }

    
    public void convertRectToScreen(Rectangle r, Component c) {
        Point p = new Point(r.x, r.y);
        SwingUtilities.convertPointToScreen(p, c);
        r.x = p.x;
        r.y = p.y;
    }

    
    public boolean compareRects(Rectangle r0, Rectangle r1) {
        int xShift = r1.x - r0.x;
        int yShift = r1.y - r0.y;

        for (int y = r0.y; y < r0.y + r0.height; y++) {
            for (int x = r0.x; x < r0.x + r0.width; x++) {
                if (!comparePixels(x, y, x + xShift, y + yShift)) {
                    return false;
                }
            }
        }
        return true;
    }

    
    public boolean comparePixels(Point p0, Point p1) {
        return comparePixels(p0.x, p0.y, p1.x, p1.y);
    }

    
    public boolean comparePixels(int x0, int y0, int x1, int y1) {
        return (getPixelColor(x0, y0).equals(getPixelColor(x1, y1)));
    }
}
