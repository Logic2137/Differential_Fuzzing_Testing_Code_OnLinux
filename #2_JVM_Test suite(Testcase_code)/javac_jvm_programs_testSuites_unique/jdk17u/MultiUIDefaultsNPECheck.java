import javax.swing.LookAndFeel;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIDefaults;
import javax.swing.SwingUtilities;

public class MultiUIDefaultsNPECheck {

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            Test();
        });
    }

    public static void Test() {
        JLabel label = new JLabel();
        try {
            UIManager.setLookAndFeel(new LookAndFeel() {

                @Override
                public String getName() {
                    return null;
                }

                @Override
                public String getID() {
                    return null;
                }

                @Override
                public String getDescription() {
                    return null;
                }

                @Override
                public boolean isNativeLookAndFeel() {
                    return false;
                }

                @Override
                public boolean isSupportedLookAndFeel() {
                    return true;
                }

                @Override
                public UIDefaults getDefaults() {
                    return null;
                }
            });
        } catch (UnsupportedLookAndFeelException e) {
            System.err.println("Warning: test not applicable because of " + "unsupported look and feel");
            return;
        }
        try {
            UIManager.getDefaults().getUI(label);
        } catch (NullPointerException e) {
            throw new RuntimeException("Got null pointer exception. Hence " + "Test Failed");
        }
    }
}
