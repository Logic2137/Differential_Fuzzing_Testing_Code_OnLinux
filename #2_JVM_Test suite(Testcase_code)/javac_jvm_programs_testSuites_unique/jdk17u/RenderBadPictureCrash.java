import java.awt.Color;
import java.awt.GraphicsDevice;
import java.lang.reflect.InvocationTargetException;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class RenderBadPictureCrash {

    public static void main(String[] args) throws InterruptedException, InvocationTargetException {
        SwingUtilities.invokeAndWait(() -> {
            JFrame f = new JFrame();
            f.setUndecorated(true);
            GraphicsDevice gd = f.getGraphicsConfiguration().getDevice();
            if (gd.isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.PERPIXEL_TRANSLUCENT)) {
                f.setBackground(new Color(0, 0, 0, 0));
            }
            f.setSize(200, 300);
            f.setVisible(true);
            try {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
            } catch (Exception e) {
                System.err.println(e);
                System.err.println("Could not set GTKLookAndFeel, skipping this test");
            } finally {
                f.dispose();
            }
        });
    }
}
