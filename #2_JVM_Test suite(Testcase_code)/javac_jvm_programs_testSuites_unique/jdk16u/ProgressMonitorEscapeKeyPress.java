



import java.awt.AWTException;
import java.awt.EventQueue;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.ProgressMonitor;
import javax.swing.SwingUtilities;

public class ProgressMonitorEscapeKeyPress {

    static ProgressMonitor monitor;
    static int counter = 0;
    static TestThread robotThread;
    static JFrame frame;


    public static void main(String[] args) throws Exception {

        createTestUI();

        monitor = new ProgressMonitor(frame, "Progress", null, 0, 100);

        robotThread = new TestThread();
        robotThread.start();

        for (counter = 0; counter <= 100; counter += 10) {
            Thread.sleep(1000);

            EventQueue.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    if (!monitor.isCanceled()) {
                        monitor.setProgress(counter);
                        System.out.println("Progress bar is in progress");
                    }
                }
            });

            if (monitor.isCanceled()) {
                break;
            }
        }

        disposeTestUI();

        if (counter >= monitor.getMaximum()) {
            throw new RuntimeException("Escape key did not cancel the ProgressMonitor");
        }
    }

     private static void createTestUI() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
           @Override
           public void run() {
                frame = new JFrame("Test");
                frame.setSize(300, 300);
                frame.setLocationByPlatform(true);
                frame.setVisible(true);
              }});
     }


     private static void disposeTestUI() throws Exception {
           SwingUtilities.invokeAndWait(() -> {
               frame.dispose();
           });
       }
}


class TestThread extends Thread {

    Robot testRobot;

    TestThread() throws AWTException {
        super();
        testRobot = new Robot();
    }

    @Override
    public void run() {
        try {
            
            Thread.sleep(5000);

            
            testRobot.keyPress(KeyEvent.VK_ESCAPE);
            testRobot.delay(20);
            testRobot.keyRelease(KeyEvent.VK_ESCAPE);
        } catch (InterruptedException ex) {
            throw new RuntimeException("Exception in TestThread");
        }
    }
}

