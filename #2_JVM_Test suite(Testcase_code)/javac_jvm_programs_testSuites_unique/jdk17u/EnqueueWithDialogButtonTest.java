import java.awt.AWTEvent;
import java.awt.Button;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class EnqueueWithDialogButtonTest {

    static Frame f;

    static Button b;

    static Dialog d;

    static Button ok;

    static CountDownLatch pressLatch = new CountDownLatch(1);

    static CountDownLatch robotLatch = new CountDownLatch(1);

    static volatile boolean gotFocus = false;

    static Robot robot;

    public static void main(String[] args) throws Exception {
        EnqueueWithDialogButtonTest test = new EnqueueWithDialogButtonTest();
        try {
            test.init();
            test.start();
        } finally {
            if (d != null) {
                d.dispose();
            }
            if (f != null) {
                f.dispose();
            }
        }
    }

    public void init() {
        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {

            public void eventDispatched(AWTEvent e) {
                if (e instanceof InputEvent) {
                    System.err.println(e.toString() + "," + ((InputEvent) e).getWhen());
                } else {
                    System.err.println(e.toString());
                }
            }
        }, AWTEvent.KEY_EVENT_MASK | AWTEvent.FOCUS_EVENT_MASK);
        f = new Frame("frame");
        f.setPreferredSize(new Dimension(100, 100));
        f.setLocation(100, 50);
        b = new Button("press");
        d = new Dialog(f, "dialog", true);
        d.setPreferredSize(new Dimension(70, 70));
        ok = new Button("ok");
        d.add(ok);
        d.pack();
        ok.addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                System.err.println("OK pressed: should arrive after got focus");
                d.dispose();
                f.dispose();
                if (gotFocus) {
                    pressLatch.countDown();
                }
            }
        });
        ok.addFocusListener(new FocusAdapter() {

            public void focusGained(FocusEvent e) {
                gotFocus = true;
                System.err.println("OK got focus");
            }
        });
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
                        EnqueueWithDialogButtonTest.this.d.toFront();
                        EnqueueWithDialogButtonTest.this.moveMouseOver(d);
                    }
                });
                d.setVisible(true);
            }
        });
    }

    public void start() throws Exception {
        robot = new Robot();
        robot.setAutoDelay(50);
        f.setVisible(true);
        waitTillShown(b);
        System.err.println("b is shown");
        f.toFront();
        moveMouseOver(f);
        robot.waitForIdle();
        robot.delay(100);
        makeFocused(b);
        robot.waitForIdle();
        robot.delay(100);
        System.err.println("b is focused");
        robot.keyPress(KeyEvent.VK_SPACE);
        robot.keyRelease(KeyEvent.VK_SPACE);
        boolean ok = robotLatch.await(1, TimeUnit.SECONDS);
        if (!ok) {
            throw new RuntimeException("Was B button pressed?");
        }
        robot.keyPress(KeyEvent.VK_SPACE);
        robot.keyRelease(KeyEvent.VK_SPACE);
        robot.delay(500);
        ok = pressLatch.await(3, TimeUnit.SECONDS);
        if (!ok) {
            throw new RuntimeException("Type-ahead doesn't work");
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
