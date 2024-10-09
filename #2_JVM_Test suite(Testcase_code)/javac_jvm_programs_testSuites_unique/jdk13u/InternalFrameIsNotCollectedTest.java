import javax.swing.*;
import java.awt.*;
import java.beans.PropertyVetoException;
import java.util.Date;

public class InternalFrameIsNotCollectedTest {

    public static final int maxWaitTime = 100000;

    public static final int waitTime = 5000;

    private static Robot robot;

    private static CustomInternalFrame iFrame;

    public static void main(String[] args) throws Exception {
        initRobot();
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                initUI();
                try {
                    closeInternalFrame();
                } catch (PropertyVetoException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        robot.waitForIdle();
        invokeGC();
        System.runFinalization();
        Thread.sleep(1000);
        Date startWaiting = new Date();
        synchronized (CustomInternalFrame.waiter) {
            Date now = new Date();
            while (now.getTime() - startWaiting.getTime() < maxWaitTime && !CustomInternalFrame.finalized) {
                CustomInternalFrame.waiter.wait(waitTime);
                now = new Date();
            }
        }
        if (!CustomInternalFrame.finalized) {
            throw new RuntimeException("Closed internal frame wasn't collected");
        }
    }

    private static void initRobot() throws AWTException {
        robot = new Robot();
        robot.setAutoDelay(100);
    }

    private static void closeInternalFrame() throws PropertyVetoException {
        iFrame.setClosed(true);
        iFrame = null;
    }

    private static void initUI() {
        JFrame frame = new JFrame("Internal Frame Test");
        frame.getContentPane().setLayout(new BorderLayout());
        JDesktopPane desktopPane = new JDesktopPane();
        desktopPane.setDesktopManager(new DefaultDesktopManager());
        frame.getContentPane().add(desktopPane, BorderLayout.CENTER);
        iFrame = new CustomInternalFrame("Dummy Frame");
        iFrame.setSize(200, 200);
        iFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        desktopPane.add(iFrame);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
        iFrame.setVisible(true);
    }

    private static void invokeGC() {
        System.out.println("Firing garbage collection!");
        try {
            StringBuilder sb = new StringBuilder();
            while (true) {
                sb.append("any string. some test. a little bit more text." + sb.toString());
            }
        } catch (Throwable e) {
        }
    }

    public static class CustomInternalFrame extends JInternalFrame {

        public static volatile boolean finalized = false;

        public static Object waiter = new Object();

        public CustomInternalFrame(String title) {
            super(title, true, true, true, true);
        }

        protected void finalize() {
            System.out.println("Finalized!");
            finalized = true;
            waiter.notifyAll();
        }
    }
}
