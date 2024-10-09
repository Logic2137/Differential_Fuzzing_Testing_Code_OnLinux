import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.awt.event.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class FreezeTest {

    static Frame f;

    static Button b;

    static Dialog d;

    static TextField tf;

    static CountDownLatch robotLatch = new CountDownLatch(1);

    static Robot robot;

    static int click_count = 100;

    static int deliver_count = 0;

    public static void main(String[] args) throws Exception {
        FreezeTest test = new FreezeTest();
        test.init();
        test.start();
    }

    public void init() {
        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {

            public void eventDispatched(AWTEvent e) {
                if (e instanceof KeyEvent) {
                    deliver_count++;
                    System.err.println("key_event# " + deliver_count);
                }
                if (e instanceof InputEvent) {
                    System.err.println(e.toString() + "," + ((InputEvent) e).getWhen());
                } else {
                    System.err.println(e.toString());
                }
            }
        }, AWTEvent.KEY_EVENT_MASK | AWTEvent.FOCUS_EVENT_MASK);
        f = new Frame("frame");
        b = new Button("press");
        d = new Dialog(f, "dialog", true);
        tf = new TextField("");
        d.add(tf);
        d.pack();
        f.add(b);
        f.pack();
        b.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.err.println(e.toString() + "," + e.getWhen());
                System.err.println("B pressed");
                robotLatch.countDown();
                EventQueue.invokeLater(new Runnable() {

                    public void run() {
                        waitTillShown(d);
                        FreezeTest.this.d.toFront();
                        FreezeTest.this.moveMouseOver(d);
                    }
                });
                d.setVisible(true);
            }
        });
    }

    public void start() throws Exception {
        robot = new Robot();
        f.setVisible(true);
        waitTillShown(b);
        System.err.println("b is shown");
        f.toFront();
        moveMouseOver(f);
        robot.waitForIdle();
        makeFocused(b);
        robot.waitForIdle();
        System.err.println("b is focused");
        robot.keyPress(KeyEvent.VK_SPACE);
        robot.keyRelease(KeyEvent.VK_SPACE);
        boolean ok = robotLatch.await(1, TimeUnit.SECONDS);
        if (!ok) {
            throw new RuntimeException("Was B button pressed?");
        }
        for (int i = 0; i < click_count; i++) {
            System.err.println("click# " + (i + 1));
            robot.keyPress(KeyEvent.VK_SPACE);
            robot.delay(10);
            robot.keyRelease(KeyEvent.VK_SPACE);
            robot.delay(50);
        }
        robot.waitForIdle();
        int deliver_count = this.deliver_count;
        int expected_count = (click_count + 1) * 3;
        if (deliver_count != expected_count) {
            System.err.println("deliver_count = " + deliver_count + " (!=" + expected_count + ")");
            throw new RuntimeException("incorrect behaviour");
        }
    }

    private void moveMouseOver(Container c) {
        Point p = c.getLocationOnScreen();
        Dimension d = c.getSize();
        robot.mouseMove(p.x + (int) (d.getWidth() / 2), p.y + (int) (d.getHeight() / 2));
    }

    private void waitTillShown(Component c) {
        while (true) {
            try {
                Thread.sleep(100);
                c.getLocationOnScreen();
                break;
            } catch (InterruptedException ie) {
                ie.printStackTrace();
                break;
            } catch (Exception e) {
            }
        }
    }

    private void makeFocused(Component comp) {
        if (comp.isFocusOwner()) {
            return;
        }
        final Semaphore sema = new Semaphore();
        final FocusAdapter fa = new FocusAdapter() {

            public void focusGained(FocusEvent fe) {
                sema.raise();
            }
        };
        comp.addFocusListener(fa);
        comp.requestFocusInWindow();
        if (comp.isFocusOwner()) {
            return;
        }
        try {
            sema.doWait(3000);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
        comp.removeFocusListener(fa);
        if (!comp.isFocusOwner()) {
            throw new RuntimeException("Can't make " + comp + " focused, current owner is " + KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner());
        }
    }

    static class Semaphore {

        boolean state = false;

        int waiting = 0;

        public Semaphore() {
        }

        public synchronized void doWait() throws InterruptedException {
            if (state) {
                return;
            }
            waiting++;
            wait();
            waiting--;
        }

        public synchronized void doWait(int timeout) throws InterruptedException {
            if (state) {
                return;
            }
            waiting++;
            wait(timeout);
            waiting--;
        }

        public synchronized void raise() {
            state = true;
            if (waiting > 0) {
                notifyAll();
            }
        }

        public synchronized boolean getState() {
            return state;
        }
    }
}
