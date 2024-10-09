




import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.SwingUtilities;

class CustomTableModel extends DefaultTableModel
{
    public CustomTableModel(Object[][] data, Object[] columnNames) {
        super(data, columnNames);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (getRowCount() > 0) {
            return getValueAt(0, columnIndex).getClass();
        }
        return super.getColumnClass(columnIndex);
    }
}

public class DefaultRowSorterCacheTest {

    public void testSort() {
        Object[] values = new Object[]{1, 2, 10};
        Object[][] data = new Object[][]{{values[0]}, {values[1]}, {values[2]}};

        
        DefaultTableModel model = new CustomTableModel(data, new Object[]{"A"});

        TableRowSorter<DefaultTableModel> rowSorter =
                new TableRowSorter<DefaultTableModel>(model);
        rowSorter.toggleSortOrder(0);

        for (int row = 0; row < model.getRowCount(); row++) {
            
            
            if (row != rowSorter.convertRowIndexToView(row)) {
                throw new RuntimeException("Wrong sorting before making any " +
                        "changes in test case");
            }
        }

        
        model.setRowCount(0);
        rowSorter.rowsDeleted(0, values.length - 1);

        
        for (int i = 0; i < values.length; i++) {
            model.addRow(new Object[]{values[i]});
            rowSorter.rowsInserted(i, i);
        }

        for (int row = 0; row < model.getRowCount(); row++) {
            
            
            if (row != rowSorter.convertRowIndexToView(row)) {
                throw new RuntimeException("Wrong sorting at end of test case");
            }
        }
    }

    public static void main(String[] args) throws Exception{
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                new DefaultRowSorterCacheTest().testSort();
            }
        });
    }
}
