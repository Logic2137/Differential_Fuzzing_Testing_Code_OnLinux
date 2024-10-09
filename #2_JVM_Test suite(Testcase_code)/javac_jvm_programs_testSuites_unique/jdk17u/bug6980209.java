import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.logging.Logger;

public class bug6980209 implements ActionListener {

    private final static Logger log = Logger.getLogger("java.awt.event.WaitDispatchSupport");

    public static final int ATTEMPTS = 100;

    public static final int EVENTS = 5;

    private static boolean runInEDT;

    private static JFrame frame;

    private static int disorderCounter = 0;

    private static Boolean enterReturn;

    private static Boolean exitReturn;

    private static int dispatchedEvents;

    private static JButton button;

    private static Point point;

    public static void main(String[] args) throws Exception {
        System.out.println("PLEASE DO NOT TOUCH KEYBOARD AND MOUSE DURING THE TEST RUN!");
        try {
            SwingUtilities.invokeAndWait(new Runnable() {

                public void run() {
                    frame = new JFrame();
                    frame.setUndecorated(true);
                    setup(frame);
                }
            });
            final Robot robot = new Robot();
            robot.delay(100);
            robot.waitForIdle();
            robot.setAutoDelay(10);
            robot.setAutoWaitForIdle(true);
            SwingUtilities.invokeAndWait(new Runnable() {

                @Override
                public void run() {
                    point = button.getLocationOnScreen();
                }
            });
            robot.mouseMove(point.x + 5, point.y + 5);
            robot.mousePress(InputEvent.BUTTON1_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_MASK);
            robot.delay(100);
            robot.waitForIdle();
            testExitBeforeEnter();
            System.out.println("Run random test in EDT");
            runInEDT = true;
            testRandomly();
            System.out.println("Run random test in another thread");
            runInEDT = false;
            testRandomly();
            System.out.println("ok");
        } finally {
            SwingUtilities.invokeAndWait(new Runnable() {

                @Override
                public void run() {
                    frame.dispose();
                }
            });
        }
    }

    private static void testExitBeforeEnter() throws Exception {
        final SecondaryLoop loop = Toolkit.getDefaultToolkit().getSystemEventQueue().createSecondaryLoop();
        loop.exit();
        Robot robot = new Robot();
        robot.mouseWheel(1);
        robot.waitForIdle();
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                if (loop.enter()) {
                    throw new RuntimeException("Wrong enter() return value");
                }
            }
        });
    }

    private static void testRandomly() throws AWTException {
        disorderCounter = 0;
        final Robot robot = new Robot();
        robot.setAutoDelay(1);
        for (int i = 0; i < ATTEMPTS; i++) {
            enterReturn = null;
            exitReturn = null;
            dispatchedEvents = 0;
            synchronized (bug6980209.class) {
                try {
                    for (int j = 0; j < EVENTS; j++) {
                        robot.keyPress(KeyEvent.VK_1);
                        robot.keyRelease(KeyEvent.VK_1);
                    }
                    robot.keyPress(KeyEvent.VK_SPACE);
                    robot.keyRelease(KeyEvent.VK_SPACE);
                    for (int j = 0; j < EVENTS; j++) {
                        robot.keyPress(KeyEvent.VK_1);
                        robot.keyRelease(KeyEvent.VK_1);
                    }
                    long time = System.nanoTime();
                    bug6980209.class.wait(1000);
                    if (enterReturn == null) {
                        System.out.println("wait time=" + ((System.nanoTime() - time) / 1E9) + " seconds");
                        throw new RuntimeException("It seems the secondary loop will never end");
                    }
                    if (!enterReturn)
                        disorderCounter++;
                    robot.waitForIdle();
                    if (dispatchedEvents < 2 * EVENTS) {
                        throw new RuntimeException("KeyEvent.VK_1 has been lost!");
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException("Interrupted!");
                }
            }
        }
        if (disorderCounter == 0) {
            System.out.println("Zero disordered enter/exit caught. It is recommended to run scenario again");
        } else {
            System.out.println("Disordered calls is " + disorderCounter + " from " + ATTEMPTS);
        }
    }

    private static void setup(final JFrame frame) {
        button = new JButton("Button");
        frame.getContentPane().add(button);
        button.addActionListener(new bug6980209());
        frame.pack();
        frame.setVisible(true);
        button.setFocusable(true);
        button.requestFocus();
        button.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == '1')
                    dispatchedEvents++;
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyChar() == '1')
                    dispatchedEvents++;
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (runInEDT) {
            runSecondaryLoop();
            return;
        }
        new Thread("Secondary loop run thread") {

            @Override
            public void run() {
                runSecondaryLoop();
            }
        }.start();
    }

    private static void runSecondaryLoop() {
        log.fine("\n---TEST START---");
        final SecondaryLoop loop = Toolkit.getDefaultToolkit().getSystemEventQueue().createSecondaryLoop();
        final Object LOCK = new Object();
        Thread exitThread = new Thread("Exit thread") {

            @Override
            public void run() {
                synchronized (LOCK) {
                    LOCK.notify();
                }
                Thread.yield();
                exitReturn = loop.exit();
                log.fine("exit() returns " + exitReturn);
            }
        };
        synchronized (LOCK) {
            try {
                exitThread.start();
                LOCK.wait();
            } catch (InterruptedException e1) {
                throw new RuntimeException("What?");
            }
        }
        enterReturn = loop.enter();
        log.fine("enter() returns " + enterReturn);
        try {
            exitThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException("What?");
        }
        synchronized (bug6980209.class) {
            bug6980209.class.notifyAll();
        }
        log.fine("\n---TEST END---");
    }
}
