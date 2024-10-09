import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class TestMultiScreenGConfigNotify {

    static JFrame f;

    static String[] propName;

    static int propCount = 0;

    static boolean result = false;

    public static void main(String[] args) throws Exception {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gds = ge.getScreenDevices();
        if (gds.length < 2) {
            return;
        }
        GraphicsConfiguration gc = gds[0].getDefaultConfiguration();
        GraphicsConfiguration gc2 = gds[1].getDefaultConfiguration();
        propName = new String[10];
        SwingUtilities.invokeAndWait(() -> {
            f = new JFrame();
            f.setSize(300, 300);
            f.setBounds(gc.getBounds().x, gc.getBounds().y, f.getWidth(), f.getHeight());
            f.setVisible(true);
            f.addPropertyChangeListener((PropertyChangeEvent evt) -> {
                String name = evt.getPropertyName();
                System.out.println("propertyChange " + name);
                propName[propCount] = name;
                propCount++;
            });
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
            }
            f.setBounds(gc2.getBounds().x, gc2.getBounds().y, f.getWidth(), f.getHeight());
        });
        Thread.sleep(1000);
        for (int i = 0; i < propCount; i++) {
            if (propName[i].equals("graphicsConfiguration")) {
                result = true;
            }
        }
        SwingUtilities.invokeAndWait(() -> f.dispose());
        if (!result) {
            throw new RuntimeException("graphicsConfiguration notification not sent");
        }
    }
}
