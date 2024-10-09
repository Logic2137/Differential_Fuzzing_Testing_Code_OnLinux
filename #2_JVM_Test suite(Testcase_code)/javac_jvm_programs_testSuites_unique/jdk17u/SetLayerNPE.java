import java.awt.EventQueue;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

public final class SetLayerNPE {

    public static void main(final String[] args) throws Exception {
        EventQueue.invokeAndWait(() -> {
            try {
                new JInternalFrame("My Frame").setLayer(null);
                throw new AssertionError("expected NPE was not thrown");
            } catch (final NullPointerException ignored) {
            }
        });
        EventQueue.invokeAndWait(() -> {
            try {
                JInternalFrame jif = new JInternalFrame("My Frame");
                new JDesktopPane().add(jif);
                jif.setLayer(null);
                throw new AssertionError("expected NPE was not thrown");
            } catch (final NullPointerException ignored) {
            }
        });
    }
}
