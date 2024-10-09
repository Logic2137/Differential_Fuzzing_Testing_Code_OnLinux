import javax.swing.SwingUtilities;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import java.util.Objects;

public class GetSelectedValueTest {

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {

            public void run() {
                DefaultListModel dlm = new DefaultListModel();
                JList list = new JList<String>(dlm);
                list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
                dlm.addElement("1");
                dlm.addElement("2");
                list.setSelectionInterval(0, 2);
                checkSelectedIndex(list, "1");
                list.setSelectionInterval(4, 5);
                checkSelectedIndex(list, null);
            }
        });
    }

    static void checkSelectedIndex(JList list, Object value) throws RuntimeException {
        Object selectedObject = list.getSelectedValue();
        if (!Objects.equals(value, selectedObject)) {
            System.out.println("Expected: " + value);
            System.out.println("Actual: " + selectedObject);
            throw new RuntimeException("Wrong selection");
        }
    }
}
