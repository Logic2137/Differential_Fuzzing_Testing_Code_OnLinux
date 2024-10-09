

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;


public final class WrongBackgroundColor {

    public static void main(final String[] args)
            throws InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(() -> {
            UIDefaults ui = UIManager.getDefaults();
            ui.put("control", new ColorUIResource(54, 54, 54));
            final JDialog dialog = new JDialog();
            final JFrame frame = new JFrame();
            frame.pack();
            dialog.pack();
            final Color dialogBackground = dialog.getBackground();
            final Color frameBackground = frame.getBackground();
            frame.dispose();
            dialog.dispose();
            if (!dialogBackground.equals(frameBackground)) {
                System.err.println("Expected:" + frameBackground);
                System.err.println("Actual:" + dialogBackground);
                throw new RuntimeException("Wrong background color");
            }
        });
    }
}
