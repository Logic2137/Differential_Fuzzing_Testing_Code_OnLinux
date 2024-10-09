import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Robot;
import java.awt.TextArea;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class InputVerifierTest {

    static volatile boolean ivWasCalled = false;

    private static void init() {
        JFrame frame = new JFrame();
        JTextField t1 = new JTextField();
        t1.setInputVerifier(new InputVerifier() {

            public boolean verify(JComponent input) {
                System.out.println("verify(" + input + ")");
                ivWasCalled = true;
                return true;
            }
        });
        JTextField t2 = new JTextField();
        frame.getContentPane().add(t1, BorderLayout.NORTH);
        frame.getContentPane().add(t2, BorderLayout.SOUTH);
        frame.setSize(200, 200);
        frame.setVisible(true);
        Robot r = null;
        try {
            r = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
            InputVerifierTest.fail(e.toString());
        }
        try {
            r.waitForIdle();
            mouseClickOnComp(r, t1);
            r.waitForIdle();
            if (!t1.isFocusOwner()) {
                throw new RuntimeException("t1 is not a focus owner");
            }
            ivWasCalled = false;
            r.keyPress(KeyEvent.VK_TAB);
            r.delay(10);
            r.keyRelease(KeyEvent.VK_TAB);
            r.waitForIdle();
            if (!t2.isFocusOwner()) {
                throw new RuntimeException("t2 is not a focus owner");
            }
            if (!ivWasCalled) {
                throw new RuntimeException("InputVerifier was not called after tabbing");
            }
            mouseClickOnComp(r, t1);
            r.waitForIdle();
            if (!t1.isFocusOwner()) {
                throw new RuntimeException("t1 is not a focus owner");
            }
            ivWasCalled = false;
            mouseClickOnComp(r, t2);
            r.waitForIdle();
            if (!t2.isFocusOwner()) {
                throw new RuntimeException("t2 is not a focus owner");
            }
            if (!ivWasCalled) {
                throw new RuntimeException("InputVErifier was not called after mouse press");
            }
        } catch (Exception e) {
            e.printStackTrace();
            InputVerifierTest.fail(e.toString());
        }
        InputVerifierTest.pass();
    }

    static void mouseClickOnComp(Robot r, Component comp) {
        Point loc = comp.getLocationOnScreen();
        loc.x += comp.getWidth() / 2;
        loc.y += comp.getHeight() / 2;
        r.mouseMove(loc.x, loc.y);
        r.delay(10);
        r.mousePress(InputEvent.BUTTON1_MASK);
        r.delay(10);
        r.mouseRelease(InputEvent.BUTTON1_MASK);
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
