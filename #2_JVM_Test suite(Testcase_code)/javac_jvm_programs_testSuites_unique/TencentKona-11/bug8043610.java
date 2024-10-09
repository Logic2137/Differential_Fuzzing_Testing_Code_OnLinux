import sun.awt.SunToolkit;
import javax.swing.*;
import java.awt.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

public class bug8043610 {

    private static volatile JFrame frame;

    private static volatile JComponent component;

    public static void main(String[] args) throws Exception {
        ThreadGroup stubTG = new ThreadGroup(getRootThreadGroup(), "Stub Thread Group");
        ThreadGroup swingTG = new ThreadGroup(getRootThreadGroup(), "SwingTG");
        try {
            Thread stubThread = new Thread(stubTG, SunToolkit::createNewAppContext);
            stubThread.start();
            stubThread.join();
            CountDownLatch startSwingLatch = new CountDownLatch(1);
            new Thread(swingTG, () -> {
                SunToolkit.createNewAppContext();
                SwingUtilities.invokeLater(() -> {
                    frame = new JFrame();
                    component = new JLabel("Test Text");
                    frame.add(component);
                    frame.setBounds(100, 100, 100, 100);
                    frame.setVisible(true);
                    startSwingLatch.countDown();
                });
            }).start();
            startSwingLatch.await();
            AtomicReference<Exception> caughtException = new AtomicReference<>();
            Thread checkThread = new Thread(getRootThreadGroup(), () -> {
                try {
                    component.invalidate();
                    component.revalidate();
                    component.repaint(new Rectangle(0, 0, 0, 0));
                } catch (Exception e) {
                    caughtException.set(e);
                }
            });
            checkThread.start();
            checkThread.join();
            if (caughtException.get() != null) {
                throw new RuntimeException("Failed. Caught exception!", caughtException.get());
            }
        } finally {
            new Thread(swingTG, () -> SwingUtilities.invokeLater(() -> {
                if (frame != null) {
                    frame.dispose();
                }
            })).start();
        }
    }

    private static ThreadGroup getRootThreadGroup() {
        ThreadGroup currentTG = Thread.currentThread().getThreadGroup();
        ThreadGroup parentTG = currentTG.getParent();
        while (parentTG != null) {
            currentTG = parentTG;
            parentTG = currentTG.getParent();
        }
        return currentTG;
    }
}
