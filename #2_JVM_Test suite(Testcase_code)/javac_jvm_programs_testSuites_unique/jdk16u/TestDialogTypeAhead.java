



import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.InvocationTargetException;

public class TestDialogTypeAhead {
    
    static Frame f;
    static Button b;
    static Dialog d;
    static Button ok;
    static Semaphore pressSema = new Semaphore();
    static Semaphore robotSema = new Semaphore();
    static volatile boolean gotFocus = false;
    static Robot robot;

    public static void main(final String[] args) {
        TestDialogTypeAhead app = new TestDialogTypeAhead();
        app.init();
        app.start();
    }

    public void init()
    {
        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
                public void eventDispatched(AWTEvent e) {
                    System.err.println(e.toString());
                }
            }, AWTEvent.KEY_EVENT_MASK);

        KeyboardFocusManager.setCurrentKeyboardFocusManager(new TestKFM());

        f = new Frame("frame");
        b = new Button("press");
        d = new Dialog(f, "dialog", true);
        ok = new Button("ok");
        d.add(ok);
        d.pack();

        ok.addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent e) {
                    System.err.println("OK pressed");
                    d.dispose();
                    f.dispose();
                    
                    
                    if (gotFocus) {
                        pressSema.raise();
                    }
                }
            });
        ok.addFocusListener(new FocusAdapter() {
                public void focusGained(FocusEvent e) {
                    gotFocus = true;
                    System.err.println("Ok got focus");
                }
            });
        f.add(b);
        f.pack();
        b.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.err.println("B pressed");

                    EventQueue.invokeLater(new Runnable() {
                            public void run() {
                                waitTillShown(d);
                                TestDialogTypeAhead.this.d.toFront();
                                TestDialogTypeAhead.this.moveMouseOver(d);
                            }
                        });

                    d.setVisible(true);
                }
            });

    }

    public void start ()
    {
        try {
            robot = new Robot();
        } catch (Exception e) {
            throw new RuntimeException("Can't create robot:" + e);
        }
        f.setLocationRelativeTo(null);
        f.setVisible(true);
        waitTillShown(b);
        System.err.println("b is shown");
        f.toFront();
        moveMouseOver(f);
        waitForIdle();
        makeFocused(b);
        waitForIdle();
        System.err.println("b is focused");

        robot.keyPress(KeyEvent.VK_SPACE);
        robot.keyRelease(KeyEvent.VK_SPACE);
        try {
            robotSema.doWait(1000);
        } catch (InterruptedException ie) {
            throw new RuntimeException("Interrupted!");
        }
        if (!robotSema.getState()) {
            throw new RuntimeException("robotSema hasn't been triggered");
        }

        System.err.println("typing ahead");
        robot.keyPress(KeyEvent.VK_SPACE);
        robot.keyRelease(KeyEvent.VK_SPACE);
        waitForIdle();
        try {
            pressSema.doWait(3000);
        } catch (InterruptedException ie) {
            throw new RuntimeException("Interrupted!");
        }
        if (!pressSema.getState()) {
            throw new RuntimeException("Type-ahead doesn't work");
        }

    }

    private void moveMouseOver(Container c) {
        Point p = c.getLocationOnScreen();
        Dimension d = c.getSize();
        robot.mouseMove(p.x + (int)(d.getWidth()/2), p.y + (int)(d.getHeight()/2));
    }
    private void waitForIdle() {
        try {
            robot.waitForIdle();
            EventQueue.invokeAndWait( new Runnable() {
                                            public void run() {
                                                
                                            }
                                        } );
        } catch(InterruptedException ite) {
            System.err.println("Robot.waitForIdle, non-fatal exception caught:");
            ite.printStackTrace();
        } catch(InvocationTargetException ine) {
            System.err.println("Robot.waitForIdle, non-fatal exception caught:");
            ine.printStackTrace();
        }
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

    class TestKFM extends DefaultKeyboardFocusManager {
        protected synchronized void enqueueKeyEvents(long after,
                                                     Component untilFocused)
        {
            super.enqueueKeyEvents(after, untilFocused);

            if (untilFocused == TestDialogTypeAhead.this.ok) {
                TestDialogTypeAhead.this.robotSema.raise();
            }
        }
    }
}

