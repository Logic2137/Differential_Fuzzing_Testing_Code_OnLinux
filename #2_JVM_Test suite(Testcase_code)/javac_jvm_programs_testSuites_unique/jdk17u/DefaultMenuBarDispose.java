import java.awt.Desktop;
import java.lang.reflect.InvocationTargetException;
import javax.swing.JMenuBar;
import javax.swing.SwingUtilities;

public class DefaultMenuBarDispose {

    public DefaultMenuBarDispose() {
        Thread watcher = new Thread(() -> {
            try {
                synchronized (this) {
                    wait(5000);
                }
                throw new RuntimeException("Test failed: failed to exit");
            } catch (InterruptedException ex) {
            }
        });
        watcher.setDaemon(true);
        watcher.start();
        runSwing(() -> {
            JMenuBar mb = new JMenuBar();
            Desktop.getDesktop().setDefaultMenuBar(mb);
            Desktop.getDesktop().setDefaultMenuBar(null);
        });
    }

    private static void runSwing(Runnable r) {
        try {
            SwingUtilities.invokeAndWait(r);
        } catch (InterruptedException e) {
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        if (!System.getProperty("os.name").contains("OS X")) {
            System.out.println("This test is for MacOS only. Automatically passed on other platforms.");
            return;
        }
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        new DefaultMenuBarDispose();
    }
}
