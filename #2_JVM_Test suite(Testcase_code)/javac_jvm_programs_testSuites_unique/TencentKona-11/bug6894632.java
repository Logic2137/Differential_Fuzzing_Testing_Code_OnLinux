



import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.util.ArrayList;
import java.util.List;

public class bug6894632 {
    private static JTable table;

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {

                
                test(new ArrayList<RowSorter.SortKey>());

                
                List<RowSorter.SortKey> sortKeys = new ArrayList<>();
                sortKeys.add(0, new RowSorter.SortKey(0, SortOrder.UNSORTED));
                test(sortKeys);
            }
        });

        System.out.println("ok");
    }

    static void test(final List<RowSorter.SortKey> sortKeys) {
        final JFrame frame = new JFrame();
        try {
            frame.setUndecorated(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            table = new JTable();
            DefaultTableModel tableModel =
                    new DefaultTableModel(10, 1) {
                        public Object getValueAt(int row, int column) {
                            return row == getRowCount() - 1 ? row + "==last" :
                                    row;
                        }
                    };
            table.setModel(tableModel);
            TableRowSorter<TableModel> sorter =
                    new TableRowSorter<TableModel>(tableModel);
            sorter.setSortKeys(sortKeys);
            table.setRowSorter(sorter);

            frame.setContentPane(table);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            int lastRow = table.getRowCount() - 1;

            
            table.setRowSelectionInterval(lastRow, lastRow);

            
            tableModel.removeRow(lastRow - 1);
            lastRow = table.getRowCount() - 1;
            if (lastRow != table.getSelectedRow()) {
                throw new RuntimeException("last row must be still selected");
            }

            
            sortKeys.clear();
            sortKeys.add(0, new RowSorter.SortKey(0, SortOrder.ASCENDING));
            sorter.setSortKeys(sortKeys);
            
            lastRow = table.getRowCount() - 1;
            tableModel.removeRow(lastRow - 1);

            if (!table.getValueAt(table.getSelectedRow(), 0).toString()
                    .endsWith("==last")) {
                throw new RuntimeException(
                        "row ends with \"==last\" row must be still selected");
            }

            
            sortKeys.clear();
            sortKeys.add(0, new RowSorter.SortKey(0, SortOrder.UNSORTED));
            sorter.setSortKeys(sortKeys);
            
            lastRow = table.getRowCount() - 1;
            tableModel.removeRow(lastRow - 1);

            lastRow = table.getRowCount() - 1;
            if (lastRow != table.getSelectedRow()) {
                throw new RuntimeException(
                        "last row must be still selected");
            }
        } finally {
            frame.dispose();
        }
    }


}
