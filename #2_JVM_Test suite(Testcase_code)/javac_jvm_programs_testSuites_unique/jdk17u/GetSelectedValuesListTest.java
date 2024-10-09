import javax.swing.SwingUtilities;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GetSelectedValuesListTest {

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {

            public void run() {
                DefaultListModel dlm = new DefaultListModel();
                JList list = new JList<String>(dlm);
                list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
                dlm.addElement("1");
                dlm.addElement("2");
                list.setSelectionInterval(0, 2);
                checkSelection(list, List.of("1", "2"));
                list.setSelectionInterval(4, 10);
                checkSelection(list, Collections.emptyList());
                list.getSelectionModel().setSelectionInterval(0, 2);
                checkSelection(list, List.of("1", "2"));
            }
        });
    }

    static void checkSelection(JList list, List<String> selectionList) throws RuntimeException {
        List<String> listSelection = list.getSelectedValuesList();
        if (!listSelection.equals(selectionList)) {
            System.out.println("Expected: " + selectionList);
            System.out.println("Actual: " + listSelection);
            throw new RuntimeException("Wrong selection from " + "getSelectedValuesList");
        }
        Object[] arraySelection = list.getSelectedValues();
        if (!Arrays.equals(arraySelection, selectionList.toArray())) {
            System.out.println("Expected: " + selectionList);
            System.out.println("Actual: " + Arrays.asList(arraySelection));
            throw new RuntimeException("Wrong selection from " + "getSelectedValues");
        }
    }
}
