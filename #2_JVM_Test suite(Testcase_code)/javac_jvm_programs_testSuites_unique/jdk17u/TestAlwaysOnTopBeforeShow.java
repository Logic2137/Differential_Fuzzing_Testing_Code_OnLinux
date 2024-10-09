import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class TestAlwaysOnTopBeforeShow {

    private static AtomicBoolean focused = new AtomicBoolean();

    private static AtomicBoolean pressed = new AtomicBoolean();

    private static volatile Object pressedTarget;

    private static Robot robot = null;

    private static void init() {
        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {

            public void eventDispatched(AWTEvent e) {
                if (e.getID() == MouseEvent.MOUSE_PRESSED) {
                    synchronized (pressed) {
                        pressed.set(true);
                        pressedTarget = e.getSource();
                        pressed.notifyAll();
                    }
                }
            }
        }, AWTEvent.MOUSE_EVENT_MASK);
        Frame f = new Frame("always-on-top");
        f.setBounds(0, 0, 200, 200);
        f.addFocusListener(new FocusAdapter() {

            public void focusGained(FocusEvent e) {
                synchronized (focused) {
                    focused.set(true);
                    focused.notifyAll();
                }
            }
        });
        f.setAlwaysOnTop(true);
        waitForIdle(1000);
        if (focused.get()) {
            throw new RuntimeException("Always-on-top generated focus event");
        }
        f.setVisible(true);
        waitFocused(f, focused);
        focused.set(false);
        Frame f2 = new Frame("auxilary");
        f2.setBounds(100, 0, 200, 100);
        f2.setVisible(true);
        f2.toFront();
        waitForIdle(1000);
        Point location = f.getLocationOnScreen();
        Dimension size = f.getSize();
        checkOnTop(f, f2, location.x + size.width / 2, location.y + size.height / 2);
        Dialog d = new Dialog(f, "Always-on-top");
        d.pack();
        d.setBounds(0, 0, 100, 100);
        waitForIdle(1000);
        checkOnTop(f, f2, location.x + size.width / 2, location.y + size.height / 2);
        waitForIdle(1000);
        focused.set(false);
        f.setVisible(false);
        f.setAlwaysOnTop(false);
        waitForIdle(1000);
        if (focused.get()) {
            throw new RuntimeException("Always-on-top generated focus event");
        }
        TestAlwaysOnTopBeforeShow.pass();
    }

    private static void waitForIdle(int mls) {
        try {
            if (robot == null) {
                robot = new Robot();
            }
            robot.waitForIdle();
            Thread.sleep(mls);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void waitFocused(Window w, AtomicBoolean b) {
        try {
            synchronized (b) {
                if (w.isFocusOwner()) {
                    return;
                }
                b.wait(3000);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (!w.isFocusOwner()) {
            throw new RuntimeException("Can't make " + w + " focus owner");
        }
    }

    static void checkOnTop(Window ontop, Window under, int x, int y) {
        under.toFront();
        try {
            Robot robot = new Robot();
            robot.mouseMove(x, y);
            robot.mousePress(InputEvent.BUTTON1_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_MASK);
            synchronized (pressed) {
                if (pressed.get()) {
                    if (pressedTarget != ontop) {
                        throw new RuntimeException("Pressed at wrong location: " + pressedTarget);
                    }
                } else {
                    pressed.wait(5000);
                }
            }
            if (!pressed.get() || pressedTarget != ontop) {
                throw new RuntimeException("Pressed at wrong location: " + pressedTarget);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean theTestPassed = false;

    private static boolean testGeneratedInterrupt = false;

    private static String failureMessage = "";

    private static Thread mainThread = null;

    private static int sleepTime = 300000;

    public static void main(String[] args) throws InterruptedException {
        mainThread = Thread.currentThread();
        try {
            init();
        } catch (TestPassedException e) {
            return;
        }
        try {
            Thread.sleep(sleepTime);
            throw new RuntimeException("Timed out after " + sleepTime / 1000 + " seconds");
        } catch (InterruptedException e) {
            if (!testGeneratedInterrupt)
                throw e;
            testGeneratedInterrupt = false;
            if (theTestPassed == false) {
                throw new RuntimeException(failureMessage);
            }
        }
    }

    public static synchronized void setTimeoutTo(int seconds) {
        sleepTime = seconds * 1000;
    }

    public static synchronized void pass() {
        System.out.println("The test passed.");
        System.out.println("The test is over, hit  Ctl-C to stop Java VM");
        if (mainThread == Thread.currentThread()) {
            theTestPassed = true;
            throw new TestPassedException();
        }
        theTestPassed = true;
        testGeneratedInterrupt = true;
        mainThread.interrupt();
    }

    public static synchronized void fail() {
        fail("it just plain failed! :-)");
    }

    public static synchronized void fail(String whyFailed) {
        System.out.println("The test failed: " + whyFailed);
        System.out.println("The test is over, hit  Ctl-C to stop Java VM");
        if (mainThread == Thread.currentThread()) {
            throw new RuntimeException(whyFailed);
        }
        theTestPassed = false;
        testGeneratedInterrupt = true;
        failureMessage = whyFailed;
        mainThread.interrupt();
    }
}

class TestPassedException extends RuntimeException {
}
