import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class PropertyChangeListenerLockSerialization {

    private static void init() {
        File file = new File(System.getProperty("test.classes", "."), "frame.ser");
        Frame f = new Frame("Frame");
        f.setBounds(250, 50, 300, 300);
        try {
            OutputStream o = new FileOutputStream(file);
            ObjectOutputStream oo = new ObjectOutputStream(o);
            oo.writeObject(f);
            oo.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            fail("Unable to serialize the frame: " + ex);
        }
        f.dispose();
        f = null;
        try {
            ObjectInputStream i = new ObjectInputStream(new FileInputStream(file));
            f = (Frame) i.readObject();
            f.show();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            fail("The NullPointerException has been thrown: " + ex);
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Error while deserializing the frame: " + ex);
        }
        f.dispose();
        f = null;
        pass();
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
