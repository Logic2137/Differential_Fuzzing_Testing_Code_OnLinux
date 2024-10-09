import java.awt.EventQueue;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import static javax.swing.UIManager.getInstalledLookAndFeels;

public final class bug4300666 {

    public static void main(final String[] args) throws Exception {
        for (UIManager.LookAndFeelInfo laf : getInstalledLookAndFeels()) {
            EventQueue.invokeAndWait(() -> setLookAndFeel(laf));
            EventQueue.invokeAndWait(() -> {
                UIDefaults d = UIManager.getDefaults();
                d.toString();
            });
        }
    }

    private static void setLookAndFeel(UIManager.LookAndFeelInfo laf) {
        try {
            UIManager.setLookAndFeel(laf.getClassName());
        } catch (UnsupportedLookAndFeelException ignored) {
            System.out.println("Unsupported L&F: " + laf.getClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
