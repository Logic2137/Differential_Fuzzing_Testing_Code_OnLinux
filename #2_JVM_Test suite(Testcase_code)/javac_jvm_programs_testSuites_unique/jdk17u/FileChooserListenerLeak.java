import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

public final class FileChooserListenerLeak {

    public static void main(final String[] args) throws Exception {
        EventQueue.invokeAndWait(() -> {
            JFileChooser chooser = new JFileChooser();
            checkListenersCount(chooser);
            LookAndFeelInfo[] infos = UIManager.getInstalledLookAndFeels();
            for (int i = 0; i < 100; ++i) {
                for (LookAndFeelInfo installedLookAndFeel : infos) {
                    setLookAndFeel(installedLookAndFeel);
                    SwingUtilities.updateComponentTreeUI(chooser);
                }
            }
            checkListenersCount(chooser);
        });
    }

    private static void checkListenersCount(Component comp) {
        test(comp.getComponentListeners());
        test(comp.getFocusListeners());
        test(comp.getHierarchyListeners());
        test(comp.getHierarchyBoundsListeners());
        test(comp.getKeyListeners());
        test(comp.getMouseListeners());
        test(comp.getMouseMotionListeners());
        test(comp.getMouseWheelListeners());
        test(comp.getInputMethodListeners());
        test(comp.getPropertyChangeListeners());
        if (comp instanceof JComponent) {
            test(((JComponent) comp).getAncestorListeners());
            test(((JComponent) comp).getVetoableChangeListeners());
        }
        if (comp instanceof JMenuItem) {
            test(((JMenuItem) comp).getMenuKeyListeners());
            test(((JMenuItem) comp).getMenuDragMouseListeners());
        }
        if (comp instanceof JMenu) {
            test(((JMenu) comp).getMenuListeners());
        }
        if (comp instanceof Container) {
            for (Component child : ((Container) comp).getComponents()) {
                checkListenersCount(child);
            }
        }
    }

    private static void test(Object[] listeners) {
        int length = listeners.length;
        if (length > 20) {
            throw new RuntimeException("The count of listeners is: " + length);
        }
    }

    private static void setLookAndFeel(UIManager.LookAndFeelInfo laf) {
        try {
            UIManager.setLookAndFeel(laf.getClassName());
        } catch (UnsupportedLookAndFeelException ignored) {
            System.out.println("Unsupported LookAndFeel: " + laf.getClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
