



import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class DefaultRowSorterIOOBEtest extends TableRowSorter<TableModel> {
    static List<String> rows = new ArrayList<>();

    static TableModel tableModel = new AbstractTableModel() {

        @Override
        public int getRowCount() {
            return rows.size();
        }

        @Override
        public int getColumnCount() {
            return 1;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return rows.get(rowIndex);
        }
    };

    public static void main(String[] args) {
        DefaultRowSorter<TableModel, Integer> sorter =
            new DefaultRowSorter<>() {
            {
                setModelWrapper(new SorterModelWrapper());
            }
        };

        PrintStream err = System.err;
        ByteArrayOutputStream bos = new ByteArrayOutputStream(10000) {
            @Override
            public synchronized void write(byte[] b, int off, int len) {
                super.write(b, off, len);
                err.print(new String(b, off, len));
            }
        };
        System.setErr(new PrintStream(bos));

        rows.add("New");

        sorter.convertRowIndexToView(0);
        sorter.convertRowIndexToModel(0);

        String out = new String(bos.toByteArray());
        if(out.indexOf("WARNING:") < 0) {
            throw new RuntimeException("No warnings found");
        }
    }

    static class SorterModelWrapper extends
                            DefaultRowSorter.ModelWrapper<TableModel, Integer> {

        @Override
        public TableModel getModel() {
            return tableModel;
        }

        @Override
        public int getColumnCount() {
            return tableModel.getColumnCount();
        }

        @Override
        public int getRowCount() {
            return tableModel.getRowCount();
        }

        @Override
        public Object getValueAt(int row, int column) {
            return tableModel.getValueAt(row, column);
        }

        @Override
        public Integer getIdentifier(int row) {
            return row;
        }
    }
}
