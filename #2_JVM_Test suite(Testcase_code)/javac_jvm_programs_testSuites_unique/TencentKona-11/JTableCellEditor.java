

import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;

import javax.accessibility.AccessibleRole;
import javax.accessibility.AccessibleTable;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;


public final class JTableCellEditor {

    private static final int COUNT = 3;
    private static JTable table;
    private static JFrame frame;

    public static void main(final String[] args)
            throws InvocationTargetException, InterruptedException {
        EventQueue.invokeAndWait(() -> {
            frame = new JFrame();
            table = new JTable(testSelectionWithFilterTable());
            frame.add(table);
            frame.pack();
        });
        EventQueue.invokeAndWait(() -> table.editCellAt(1, 1));
        EventQueue.invokeAndWait(() -> {
            AccessibleTable aTable = table.getAccessibleContext()
                                          .getAccessibleTable();
            int aColumns = aTable.getAccessibleColumnCount();
            int aRows = aTable.getAccessibleRowCount();
            
            
            AccessibleRole role = aTable.getAccessibleAt(1, 1)
                                        .getAccessibleContext()
                                        .getAccessibleRole();
            frame.dispose();
            if (!role.toDisplayString(Locale.ENGLISH).equals("text")) {
                throw new RuntimeException("Unexpected role: " + role);
            }
            if (aColumns != COUNT) {
                throw new RuntimeException("Wrong columns: " + aColumns);
            }
            if (aRows != COUNT) {
                throw new RuntimeException("Wrong rows: " + aRows);
            }
        });
    }

    
    private static TableModel testSelectionWithFilterTable() {
        DefaultTableModel model = new DefaultTableModel(0, 3);
        for (int i = 0; i < COUNT; i++) {
            model.addRow(new Object[]{i + "x0", i + "x1", i + "x2"});
        }
        return model;
    }
}
