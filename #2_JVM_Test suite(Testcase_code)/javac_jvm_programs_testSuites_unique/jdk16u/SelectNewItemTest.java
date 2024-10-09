


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

public class SelectNewItemTest implements ItemListener, WindowListener {
    
    private Frame frame;
    private Choice theChoice;
    private Robot robot;

    private CountDownLatch latch = new CountDownLatch(1);
    private volatile boolean passed = false;

    private void init()
    {
        try {
            robot = new Robot();
            robot.setAutoDelay(500);
        } catch (AWTException e) {
            throw new RuntimeException("Unable to create Robot. Test fails.");
        }

        frame = new Frame("SelectNewItemTest");
        frame.setLayout(new BorderLayout());
        theChoice = new Choice();
        for (int i = 0; i < 10; i++) {
            theChoice.add(new String("Choice Item " + i));
        }
        theChoice.addItemListener(this);
        frame.add(theChoice);
        frame.addWindowListener(this);

        frame.setLocation(1,20);
        frame.setSize(200, 50);
        robot.mouseMove(10, 30);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String... args) {
        SelectNewItemTest test = new SelectNewItemTest();
        test.init();
        try {
            test.latch.await(12000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {}
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

            Point loc = theChoice.getLocationOnScreen();
            int selectedIndex = theChoice.getSelectedIndex();
            Dimension size = theChoice.getSize();

            robot.mouseMove(loc.x + size.width - 10, loc.y + size.height / 2);

            robot.setAutoDelay(250);
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

            robot.delay(1000);

            
            
            robot.mouseMove(loc.x + size.width / 2, loc.y + 3 * size.height);
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            robot.waitForIdle();

            if (selectedIndex == theChoice.getSelectedIndex())
                throw new RuntimeException("Test case failed - expected to select" +
                " a different item than " + selectedIndex);

            selectedIndex = theChoice.getSelectedIndex();
            
            
            robot.delay(1000);
            robot.mouseMove(loc.x + size.width - 10, loc.y + size.height / 2);

            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            
            
            
            
            
            
            if (System.getProperty("os.name").toLowerCase().startsWith("win")) {
                robot.mouseWheel(-100);
            }
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

            robot.delay(1000);
            robot.mouseMove(loc.x + size.width / 2, loc.y + 3 * size.height);
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            robot.waitForIdle();

            if (selectedIndex != theChoice.getSelectedIndex())
                throw new RuntimeException("Test failed. Expected to select the same item " +
                "located at: " + selectedIndex + " but got an item selected at: " + theChoice.getSelectedIndex());
        } catch(InterruptedException e) {
            throw new RuntimeException(e.getCause());
        } finally {
            latch.countDown();
        }
    }

    @Override public void itemStateChanged(ItemEvent e) {
        if (!passed) {
            System.out.println("ItemEvent received.  Test passes");
            passed = true;
        } else {
            System.out.println("ItemEvent received for second click. Test fails");
            passed = false;
        }
    }

    @Override public void windowOpened(WindowEvent e) {
        System.out.println("windowActivated()");
        (new Thread(this::run)).start();
    }

    @Override public void windowActivated(WindowEvent e) {}
    @Override public void windowDeactivated(WindowEvent e) {}
    @Override public void windowClosed(WindowEvent e) {}
    @Override public void windowClosing(WindowEvent e) {}
    @Override public void windowIconified(WindowEvent e) {}
    @Override public void windowDeiconified(WindowEvent e) {}
}
