



import com.sun.java.accessibility.util.AWTEventMonitor;
import java.awt.Dimension;
import java.lang.reflect.InvocationTargetException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Bug8051626 {

    public static void main(final String[] args) throws InterruptedException,
                                                        InvocationTargetException {
            final Bug8051626 app = new Bug8051626();
            app.test();
        }

    private void test() throws InterruptedException, InvocationTargetException {
        System.setSecurityManager(new SecurityManager());
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                final JFrame frame = new JFrame("Bug 8051626");
                try {
                    final JPanel panel = new JPanel();
                    final JButton okButton = new JButton("OK");
                    panel.add(okButton);
                    frame.getContentPane().add(panel);
                    frame.setMinimumSize(new Dimension(300, 180));
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.pack();
                    frame.setLocation(400, 300);
                    frame.setVisible(true);
                    
                    
                    
                    
                    AWTEventMonitor.getComponentWithFocus();
                } finally {
                    frame.dispose();
                }
            }
        });
    }

}
