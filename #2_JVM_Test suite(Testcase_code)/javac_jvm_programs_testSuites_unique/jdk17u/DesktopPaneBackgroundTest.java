import java.awt.Color;
import java.awt.Toolkit;
import javax.swing.JDesktopPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class DesktopPaneBackgroundTest {

    private static Color defaultBackgroudColor;

    public static void main(String[] args) throws Exception {
        defaultBackgroudColor = (Color) Toolkit.getDefaultToolkit().getDesktopProperty("win.mdi.backgroundColor");
        String[] lookAndFeel = new String[] { "com.sun.java.swing.plaf.windows.WindowsLookAndFeel", "com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel" };
        for (String laf : lookAndFeel) {
            UIManager.setLookAndFeel(laf);
            SwingUtilities.invokeAndWait(() -> {
                JDesktopPane desktopPane = new JDesktopPane();
                Color background = desktopPane.getBackground();
                if (!background.equals(defaultBackgroudColor)) {
                    throw new RuntimeException("Invalid JDesktopPane " + "Background Color for WLAF");
                }
            });
        }
    }
}
