import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;

public class bug8032874 {

    private static final int ROW_COUNT = 5;

    private static JTable table;

    private static TestTableModel tableModel;

    public static void main(String[] args) throws Exception {
        Robot robot = new Robot();
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                createAndShowUI();
            }
        });
        robot.waitForIdle();
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                table.getRowSorter().toggleSortOrder(0);
                table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
                table.setRowSelectionInterval(1, 2);
            }
        });
        robot.waitForIdle();
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                for (int i = 0; i < ROW_COUNT; i++) {
                    tableModel.remove(0);
                    table.getRowSorter().toggleSortOrder(0);
                }
            }
        });
    }

    public static void createAndShowUI() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        JFrame frame = new JFrame("bug8032874");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        tableModel = new TestTableModel();
        table = new JTable(tableModel);
        table.setSurrendersFocusOnKeystroke(true);
        final TableRowSorter<TestTableModel> rowSorter = new TableRowSorter<TestTableModel>(tableModel);
        rowSorter.setRowFilter(new RowFilter<TestTableModel, Integer>() {

            @Override
            public boolean include(Entry<? extends TestTableModel, ? extends Integer> entry) {
                return entry.getIdentifier() % 2 == 0;
            }
        });
        table.setRowSorter(rowSorter);
        JScrollPane jScrollPane = new JScrollPane(table);
        panel.add(jScrollPane);
        frame.setContentPane(panel);
        frame.setSize(new Dimension(800, 600));
        frame.setVisible(true);
    }

    private static class TestTableModel extends AbstractTableModel {

        private final List<Integer> data;

        public TestTableModel() {
            data = new ArrayList<Integer>();
            for (int i = 0; i < ROW_COUNT; i++) {
                data.add(i);
            }
        }

        @Override
        public int getRowCount() {
            return data.size();
        }

        @Override
        public int getColumnCount() {
            return 1;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return data.get(rowIndex);
        }

        public void remove(int row) {
            data.remove(row);
            fireTableRowsDeleted(row, row);
        }
    }
}
