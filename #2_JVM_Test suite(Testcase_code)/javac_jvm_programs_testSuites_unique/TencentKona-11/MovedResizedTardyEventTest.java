



import java.awt.*;
import java.awt.event.*;
import java.applet.Applet;
import java.lang.reflect.InvocationTargetException;

public class MovedResizedTardyEventTest extends Applet {
    Frame f1 = new Frame("F-1");
    Frame f2 = new Frame("F-2");

    boolean eventFlag = false;

    public static void main(String[] args) {
        Applet a = new MovedResizedTardyEventTest();
        a.start();
    }

    public void start() {
        f1.setVisible(true);
        f2.setVisible(true);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {}

        f1.addComponentListener(new ComponentAdapter() {
                public void componentMoved(ComponentEvent e) {
                    MovedResizedTardyEventTest.this.eventFlag = true;
                    System.err.println(e);
                }
                public void componentResized(ComponentEvent e) {
                    MovedResizedTardyEventTest.this.eventFlag = true;
                    System.err.println(e);
                }
            });

        f1.toFront();

        waitForIdle();

        try { 
            Thread.sleep(500);
        } catch (InterruptedException e) {}

        if (eventFlag) {
            throw new RuntimeException("Test failed!");
        }
    }

    void waitForIdle() {
        try {
            (new Robot()).waitForIdle();
            EventQueue.invokeAndWait( new Runnable() {
                    public void run() {} 
                });
        } catch(InterruptedException ie) {
            System.err.println("waitForIdle, non-fatal exception caught:");
            ie.printStackTrace();
        } catch(InvocationTargetException ite) {
            System.err.println("waitForIdle, non-fatal exception caught:");
            ite.printStackTrace();
        } catch(AWTException rex) {
            rex.printStackTrace();
            throw new RuntimeException("unexpected exception");
        }
    }
}
