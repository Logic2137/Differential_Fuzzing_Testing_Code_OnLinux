

import java.awt.EventQueue;

import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;


public final class FileChooserListenerLeak {

    public static void main(final String[] args) throws Exception {
        EventQueue.invokeAndWait(()->{
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

    private static void checkListenersCount(JFileChooser chooser) {
        test(chooser.getComponentListeners());
        test(chooser.getFocusListeners());
        test(chooser.getHierarchyListeners());
        test(chooser.getHierarchyBoundsListeners());
        test(chooser.getKeyListeners());
        test(chooser.getMouseListeners());
        test(chooser.getMouseMotionListeners());
        test(chooser.getMouseWheelListeners());
        test(chooser.getInputMethodListeners());
        test(chooser.getPropertyChangeListeners());
        test(chooser.getAncestorListeners());
        test(chooser.getVetoableChangeListeners());
    }

    
    private static void test(Object[] listeners) {
        int length = listeners.length;
        if (length > 10) {
            throw new RuntimeException("The count of listeners is: " + length);
        }
    }

    private static void setLookAndFeel(UIManager.LookAndFeelInfo laf) {
        try {
            UIManager.setLookAndFeel(laf.getClassName());
        } catch (UnsupportedLookAndFeelException ignored){
            System.out.println("Unsupported LookAndFeel: " + laf.getClassName());
        } catch (ClassNotFoundException | InstantiationException |
                IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
