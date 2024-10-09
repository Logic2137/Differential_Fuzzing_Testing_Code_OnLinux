import java.awt.AWTException;
import java.awt.Robot;
import java.awt.GraphicsDevice;
import java.awt.Toolkit;
import java.awt.Point;
import java.awt.MouseInfo;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class ExtendedRobot extends Robot {

    private static int DEFAULT_SPEED = 20;

    private static int DEFAULT_SYNC_DELAY = 500;

    private static int DEFAULT_STEP_LENGTH = 2;

    private final int syncDelay = DEFAULT_SYNC_DELAY;

    public ExtendedRobot() throws AWTException {
        super();
    }

    public ExtendedRobot(GraphicsDevice screen) throws AWTException {
        super(screen);
    }

    public int getSyncDelay() {
        return this.syncDelay;
    }

    public void click(int buttons) {
        mousePress(buttons);
        waitForIdle(DEFAULT_SPEED);
        mouseRelease(buttons);
        waitForIdle();
    }

    public void click() {
        click(InputEvent.BUTTON1_DOWN_MASK);
    }

    public synchronized void waitForIdle(int delayValue) {
        super.waitForIdle();
        delay(delayValue);
    }

    @Override
    public synchronized void waitForIdle() {
        waitForIdle(syncDelay);
    }

    public void glide(int x, int y) {
        Point p = MouseInfo.getPointerInfo().getLocation();
        glide(p.x, p.y, x, y);
    }

    public void glide(Point dest) {
        glide(dest.x, dest.y);
    }

    public void glide(int fromX, int fromY, int toX, int toY) {
        glide(fromX, fromY, toX, toY, DEFAULT_STEP_LENGTH, DEFAULT_SPEED);
    }

    public void glide(Point src, Point dest) {
        glide(src.x, src.y, dest.x, dest.y, DEFAULT_STEP_LENGTH, DEFAULT_SPEED);
    }

    public void glide(int srcX, int srcY, int destX, int destY, int stepLength, int speed) {
        int stepNum;
        double tDx, tDy;
        double dx, dy, ds;
        double x, y;
        dx = (destX - srcX);
        dy = (destY - srcY);
        ds = Math.sqrt(dx * dx + dy * dy);
        tDx = dx / ds * stepLength;
        tDy = dy / ds * stepLength;
        int stepsCount = (int) ds / stepLength;
        mouseMove(srcX, srcY);
        for (x = srcX, y = srcY, stepNum = 0; stepNum < stepsCount; stepNum++) {
            x += tDx;
            y += tDy;
            mouseMove((int) x, (int) y);
            delay(speed);
        }
        mouseMove(destX, destY);
    }

    public synchronized void mouseMove(Point position) {
        mouseMove(position.x, position.y);
    }

    public void dragAndDrop(int fromX, int fromY, int toX, int toY) {
        mouseMove(fromX, fromY);
        mousePress(InputEvent.BUTTON1_DOWN_MASK);
        waitForIdle();
        glide(toX, toY);
        mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        waitForIdle();
    }

    public void dragAndDrop(Point from, Point to) {
        dragAndDrop(from.x, from.y, to.x, to.y);
    }

    public void type(int keycode) {
        keyPress(keycode);
        waitForIdle(DEFAULT_SPEED);
        keyRelease(keycode);
        waitForIdle(DEFAULT_SPEED);
    }

    public void type(char c) {
        type(KeyEvent.getExtendedKeyCodeForChar(c));
    }

    public void type(char[] symbols) {
        for (int i = 0; i < symbols.length; i++) {
            type(symbols[i]);
        }
    }

    public void type(String s) {
        type(s.toCharArray());
    }
}
