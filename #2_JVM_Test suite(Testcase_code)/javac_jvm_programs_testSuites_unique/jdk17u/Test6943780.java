import javax.swing.JButton;
import javax.swing.JTabbedPane;
import java.awt.Component;
import static javax.swing.SwingUtilities.invokeAndWait;

public class Test6943780 implements Runnable, Thread.UncaughtExceptionHandler {

    public static void main(String[] args) throws Exception {
        invokeAndWait(new Test6943780());
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        throwable.printStackTrace();
        System.exit(1);
    }

    @Override
    public void run() {
        JTabbedPane pane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
        pane.addTab("first", new JButton("first"));
        pane.addTab("second", new JButton("second"));
        for (Component component : pane.getComponents()) {
            component.setSize(100, 100);
        }
    }
}
