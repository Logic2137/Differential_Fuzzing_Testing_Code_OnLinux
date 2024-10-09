import javax.swing.JButton;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.metal.MetalLookAndFeel;

public class FontScalingTest {

    public static void main(String[] args) throws Exception {
        int metalFontSize = getFontSize(MetalLookAndFeel.class.getName());
        int systemFontSize = getFontSize(UIManager.getSystemLookAndFeelClassName());
        if (Math.abs(systemFontSize - metalFontSize) > 8) {
            throw new RuntimeException("System L&F is too big!");
        }
    }

    private static int getFontSize(String laf) throws Exception {
        UIManager.setLookAndFeel(laf);
        final int[] sizes = new int[1];
        SwingUtilities.invokeAndWait(() -> {
            JButton button = new JButton("Test");
            sizes[0] = button.getFont().getSize();
        });
        return sizes[0];
    }
}
