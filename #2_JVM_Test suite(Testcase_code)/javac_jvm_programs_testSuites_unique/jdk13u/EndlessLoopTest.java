import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Robot;
import java.awt.TextArea;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class EndlessLoopTest {

    static volatile int n_iv_calls;

    private static void init() {
        JFrame frame = new JFrame();
        final JDialog dialog = new JDialog(frame, true);
        JButton button = new JButton("press me");
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                dialog.dispose();
            }
        });
        dialog.getContentPane().add(button);
        dialog.pack();
        JTextField t1 = new JTextField();
        t1.setInputVerifier(new InputVerifier() {

            public boolean verify(JComponent input) {
                n_iv_calls++;
                if (n_iv_calls == 1) {
                    dialog.setVisible(true);
                }
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
            EndlessLoopTest.fail(e);
        }
        try {
            r.waitForIdle();
            mouseClickOnComp(r, t1);
            r.waitForIdle();
            if (!t1.isFocusOwner()) {
                throw new RuntimeException("t1 is not a focus owner");
            }
            n_iv_calls = 0;
            r.keyPress(KeyEvent.VK_TAB);
            r.delay(10);
            r.keyRelease(KeyEvent.VK_TAB);
            r.waitForIdle();
            mouseClickOnComp(r, button);
            r.waitForIdle();
        } catch (Exception e) {
            EndlessLoopTest.fail(e);
        }
        if (n_iv_calls != 1) {
            EndlessLoopTest.fail(new RuntimeException("InputVerifier was called " + n_iv_calls + " times"));
        }
        EndlessLoopTest.pass();
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

    public static synchronized void fail(Exception whyFailed) {
        System.out.println("The test failed: " + whyFailed);
        System.out.println("The test is over, hit  Ctl-C to stop Java VM");
        if (mainThread == Thread.currentThread()) {
            throw new RuntimeException(whyFailed);
        }
        theTestPassed = false;
        testGeneratedInterrupt = true;
        failureMessage = whyFailed.toString();
        mainThread.interrupt();
    }
}

class TestPassedException extends RuntimeException {
}
