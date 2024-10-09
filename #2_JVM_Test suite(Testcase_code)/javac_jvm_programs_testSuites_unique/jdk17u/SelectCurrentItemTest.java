import java.awt.Choice;
import java.awt.Robot;
import java.awt.Frame;
import java.awt.BorderLayout;
import java.awt.AWTException;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.event.InputEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowListener;
import java.awt.event.ItemEvent;
import java.awt.event.WindowEvent;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class SelectCurrentItemTest implements ItemListener, WindowListener {

    private Frame frame;

    private Choice theChoice;

    private Robot robot;

    private CountDownLatch latch = new CountDownLatch(1);

    private volatile boolean passed = true;

    private void init() {
        try {
            robot = new Robot();
            robot.setAutoDelay(500);
        } catch (AWTException e) {
            throw new RuntimeException("Unable to create Robot. Test fails.");
        }
        frame = new Frame("SelectCurrentItemTest");
        frame.setLayout(new BorderLayout());
        theChoice = new Choice();
        for (int i = 0; i < 10; i++) {
            theChoice.add(new String("Choice Item " + i));
        }
        theChoice.addItemListener(this);
        frame.add(theChoice);
        frame.addWindowListener(this);
        frame.setLocation(1, 20);
        robot.mouseMove(10, 30);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String... args) {
        SelectCurrentItemTest test = new SelectCurrentItemTest();
        test.init();
        try {
            test.latch.await(12000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
        }
        test.robot.waitForIdle();
        try {
            if (!test.passed) {
                throw new RuntimeException("TEST FAILED.");
            }
        } finally {
            test.frame.dispose();
        }
    }

    private void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        Point loc = theChoice.getLocationOnScreen();
        Dimension size = theChoice.getSize();
        robot.mouseMove(loc.x + size.width - 10, loc.y + size.height / 2);
        robot.setAutoDelay(250);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        robot.delay(1000);
        robot.mouseMove(loc.x + size.width / 2, loc.y + size.height);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        robot.waitForIdle();
        latch.countDown();
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        System.out.println("ItemEvent received.  Test fails");
        passed = false;
    }

    @Override
    public void windowOpened(WindowEvent e) {
        System.out.println("windowActivated()");
        (new Thread(this::run)).start();
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }
}
