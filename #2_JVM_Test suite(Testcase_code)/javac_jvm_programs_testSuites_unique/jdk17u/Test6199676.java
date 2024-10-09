import java.awt.Component;
import java.awt.Container;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Test6199676 implements Runnable {

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(new Test6199676());
    }

    private static Component getPreview(Container container) {
        String name = "ColorChooser.previewPanelHolder";
        for (Component component : container.getComponents()) {
            if (!name.equals(component.getName())) {
                component = (component instanceof Container) ? getPreview((Container) component) : null;
            }
            if (component instanceof Container) {
                container = (Container) component;
                return 1 == container.getComponentCount() ? container.getComponent(0) : null;
            }
        }
        return null;
    }

    private static boolean isShowing(Component component) {
        return (component != null) && component.isShowing();
    }

    public synchronized void run() {
        JColorChooser chooser = new JColorChooser();
        JFrame frame = new JFrame(getClass().getName());
        try {
            frame.add(chooser);
            frame.setVisible(true);
            setLookAndFeel(UIManager.getInstalledLookAndFeels()[0]);
            SwingUtilities.updateComponentTreeUI(chooser);
            Component component = chooser.getPreviewPanel();
            if (component == null) {
                component = getPreview(chooser);
            }
            if (!isShowing(component)) {
                throw new RuntimeException("default preview panel is not showing");
            }
            chooser.setPreviewPanel(new JPanel());
            setLookAndFeel(UIManager.getInstalledLookAndFeels()[1]);
            SwingUtilities.updateComponentTreeUI(chooser);
            if (isShowing(chooser.getPreviewPanel())) {
                throw new RuntimeException("custom preview panel is showing");
            }
        } finally {
            frame.dispose();
        }
    }

    private static void setLookAndFeel(final UIManager.LookAndFeelInfo laf) {
        try {
            UIManager.setLookAndFeel(laf.getClassName());
        } catch (final UnsupportedLookAndFeelException ignored) {
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
