import javax.accessibility.Accessible;
import javax.accessibility.AccessibleAction;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleSelection;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.SwingUtilities;

public class Bug8161483 extends JFrame {

    private static JFrame frame = null;

    private static volatile Exception exception = null;

    private JList<String> countryList;

    public static void main(String[] args) throws Exception {
        try {
            SwingUtilities.invokeAndWait(() -> {
                DefaultListModel<String> listModel = new DefaultListModel<>();
                listModel.addElement("one");
                listModel.addElement("two");
                listModel.addElement("three");
                JList<String> list = new JList<>(listModel);
                frame = new JFrame();
                frame.add(list);
                frame.pack();
                try {
                    AccessibleContext acList = list.getAccessibleContext();
                    Accessible accChild = acList.getAccessibleChild(1);
                    AccessibleContext acChild = accChild.getAccessibleContext();
                    AccessibleAction aa = acChild.getAccessibleAction();
                    int c = aa.getAccessibleActionCount();
                    if (c != 1) {
                        throw new RuntimeException("getAccessibleActionCount is not 1");
                    }
                    String s = aa.getAccessibleActionDescription(0);
                    if (!s.equals("click")) {
                        throw new RuntimeException("getAccessibleActionDescription is not click");
                    }
                    boolean b = aa.doAccessibleAction(0);
                    if (!b) {
                        throw new RuntimeException("doAccessibleAction did not return true");
                    }
                    AccessibleSelection as = acList.getAccessibleSelection();
                    int asc = as.getAccessibleSelectionCount();
                    if (asc != 1) {
                        throw new RuntimeException("getAccessibleSelectionCount is not 1");
                    }
                    boolean isSelected = as.isAccessibleChildSelected(0);
                    if (isSelected) {
                        throw new RuntimeException("isAccessibleChildSelected(0) did not return false");
                    }
                    isSelected = as.isAccessibleChildSelected(1);
                    if (!isSelected) {
                        throw new RuntimeException("isAccessibleChildSelected(1) did not return true");
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
                if (frame != null) {
                    frame.dispose();
                }
            });
        }
    }
}
