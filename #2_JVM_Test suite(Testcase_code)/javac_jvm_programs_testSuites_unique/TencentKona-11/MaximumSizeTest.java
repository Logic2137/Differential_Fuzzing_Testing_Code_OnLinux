


import javax.swing.SwingUtilities;
import javax.swing.JRootPane;
import javax.swing.JLabel;
import java.awt.Dimension;
import java.lang.reflect.InvocationTargetException;

public class MaximumSizeTest {
    public static void main(String args[]) {
        try {
            SwingUtilities.invokeAndWait(() -> {
                JRootPane r = new JRootPane();
                r.getContentPane().add(new JLabel("foo"));
                System.out.println("Min Size: " + r.getMinimumSize());
                System.out.println("Preferred Size: " + r.getPreferredSize());

                Dimension d = r.getMaximumSize();
                if (d.width == 0 || d.height == 0) {
                    throw new RuntimeException("Maximum size is wrongly reported: " + d);
                }

                System.out.println("Max size: " + d);
            });
        } catch (InterruptedException | InvocationTargetException e) {
            System.out.println(e.getCause());
            throw new RuntimeException(e.getMessage());
        }
    }
}
