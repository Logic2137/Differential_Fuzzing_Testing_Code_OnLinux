



import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleSelection;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public class Bug8154069 {

    private static JFrame frame;
    private static volatile Exception exception = null;

    public static void main(String args[]) throws Exception {
        try {
            try {
                UIManager.setLookAndFeel(new NimbusLookAndFeel());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            SwingUtilities.invokeAndWait(() -> {
                frame = new JFrame();
                String[] petStrings = { "Bird", "Cat" };
                JComboBox<String> cb = new JComboBox<>(petStrings);
                cb.setSelectedIndex(1);  
                frame.add(cb);
                frame.pack();
                try {
                    cb.setSelectedIndex(-1);
                    int i = cb.getSelectedIndex();
                    if (i != -1) {
                        throw new RuntimeException("getSelectedIndex is not -1");
                    }
                    Object o = cb.getSelectedItem();
                    if (o != null) {
                        throw new RuntimeException("getSelectedItem is not null");
                    }
                    AccessibleContext ac = cb.getAccessibleContext();
                    AccessibleSelection as = ac.getAccessibleSelection();
                    int count = as.getAccessibleSelectionCount();
                    if (count != 0) {
                        throw new RuntimeException("getAccessibleSelection count is not 0");
                    }
                    Accessible a = as.getAccessibleSelection(0);
                    if (a != null) {
                        throw new RuntimeException("getAccessibleSelection(0) is not null");
                    }
                } catch (Exception e) {
                    exception = e;
                }
            });
            if (exception != null) {
                System.out.println("Test failed: " + exception.getMessage());
                throw exception;
            } else {
                System.out.println("Test passed.");
            }
        } finally {
            SwingUtilities.invokeAndWait(() -> {
                if (frame != null) { frame.dispose(); }
            });
        }
    }

}
