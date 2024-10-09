

import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;


public final class HangNonVolatileBuffer {

    private static JFrame f;

    public static void main(final String[] args)
            throws InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(() -> {
            f = new JFrame("JFrame");
            f.setSize(300, 300);
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
        SwingUtilities.invokeAndWait(() -> {
            
        });
        Thread.sleep(1000);
        SwingUtilities.invokeAndWait(f::dispose);
    }
}
