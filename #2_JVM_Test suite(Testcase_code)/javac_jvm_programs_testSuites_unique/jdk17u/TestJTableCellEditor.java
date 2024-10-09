import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;

public class TestJTableCellEditor {

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(TestJTableCellEditor::testJTableCellEditor);
        System.setSecurityManager(new SecurityManager());
        SwingUtilities.invokeAndWait(TestJTableCellEditor::testJTableCellEditor);
    }

    private static void testJTableCellEditor() {
        final Class cls = UserEditor.class;
        JTable table = new JTable(new AbstractTableModel() {

            public int getRowCount() {
                return 0;
            }

            public int getColumnCount() {
                return 1;
            }

            public Object getValueAt(int r, int c) {
                return "Some Value";
            }

            public Class getColumnClass(int c) {
                return cls;
            }
        });
        TableCellEditor editor = table.getDefaultEditor(Object.class);
        editor.getTableCellEditorComponent(table, UserEditor.TEST_VALUE, false, 0, 0);
        editor.stopCellEditing();
        Object obj = editor.getCellEditorValue();
        if (obj == null) {
            throw new RuntimeException("Editor object is null!");
        }
        if (!UserEditor.TEST_VALUE.equals(((UserEditor) obj).value)) {
            throw new RuntimeException("Value is incorrect!");
        }
    }

    public static class UserEditor {

        private static final String TEST_VALUE = "Test Value";

        private final String value;

        public UserEditor(String value) {
            this.value = value;
        }
    }
}
