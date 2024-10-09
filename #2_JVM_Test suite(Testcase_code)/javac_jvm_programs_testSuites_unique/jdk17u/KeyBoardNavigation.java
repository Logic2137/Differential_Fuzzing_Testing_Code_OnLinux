import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import javax.swing.DefaultCellEditor;
import javax.swing.JApplet;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

public class KeyBoardNavigation extends JApplet {

    static void initTest(Container contentPane) {
        final String[] names = { "First Name", "Last Name", "Favorite Color", "Favorite Number", "Vegetarian" };
        final Object[][] data = { { "Mark", "Andrews", "Red", new Integer(2), new Boolean(true) }, { "Tom", "Ball", "Blue", new Integer(99), new Boolean(false) }, { "Alan", "Chung", "Green", new Integer(838), new Boolean(false) }, { "Jeff", "Dinkins", "Turquois", new Integer(8), new Boolean(true) }, { "Amy", "Fowler", "Yellow", new Integer(3), new Boolean(false) }, { "Brian", "Gerhold", "Green", new Integer(0), new Boolean(false) }, { "James", "Gosling", "Pink", new Integer(21), new Boolean(false) }, { "David", "Karlton", "Red", new Integer(1), new Boolean(false) }, { "Dave", "Kloba", "Yellow", new Integer(14), new Boolean(false) }, { "Peter", "Korn", "Purple", new Integer(12), new Boolean(false) }, { "Phil", "Milne", "Purple", new Integer(3), new Boolean(false) }, { "Dave", "Moore", "Green", new Integer(88), new Boolean(false) }, { "Hans", "Muller", "Maroon", new Integer(5), new Boolean(false) }, { "Rick", "Levenson", "Blue", new Integer(2), new Boolean(false) }, { "Tim", "Prinzing", "Blue", new Integer(22), new Boolean(false) }, { "Chester", "Rose", "Black", new Integer(0), new Boolean(false) }, { "Ray", "Ryan", "Gray", new Integer(77), new Boolean(false) }, { "Georges", "Saab", "Red", new Integer(4), new Boolean(false) }, { "Willie", "Walker", "Phthalo Blue", new Integer(4), new Boolean(false) }, { "Kathy", "Walrath", "Blue", new Integer(8), new Boolean(false) }, { "Arnaud", "Weber", "Green", new Integer(44), new Boolean(false) } };
        TableModel dataModel = new AbstractTableModel() {

            public int getColumnCount() {
                return names.length;
            }

            public int getRowCount() {
                return data.length;
            }

            public Object getValueAt(int row, int col) {
                return data[row][col];
            }

            public String getColumnName(int column) {
                return names[column];
            }

            public Class getColumnClass(int c) {
                return getValueAt(0, c).getClass();
            }

            public boolean isCellEditable(int row, int col) {
                return true;
            }

            public void setValueAt(Object aValue, int row, int column) {
                System.out.println("Setting value to: " + aValue);
                data[row][column] = aValue;
            }
        };
        JTable tableView = new JTable(dataModel);
        tableView.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JComboBox comboBox = new JComboBox();
        comboBox.addItem("Red");
        comboBox.addItem("Orange");
        comboBox.addItem("Yellow");
        comboBox.addItem("Green");
        comboBox.addItem("Blue");
        comboBox.addItem("Indigo");
        comboBox.addItem("Violet");
        TableColumn colorColumn = tableView.getColumn("Favorite Color");
        colorColumn.setCellEditor(new DefaultCellEditor(comboBox));
        DefaultTableCellRenderer colorColumnRenderer = new DefaultTableCellRenderer();
        colorColumnRenderer.setBackground(Color.pink);
        colorColumnRenderer.setToolTipText("Click for combo box");
        colorColumn.setCellRenderer(colorColumnRenderer);
        TableCellRenderer headerRenderer = colorColumn.getHeaderRenderer();
        if (headerRenderer instanceof DefaultTableCellRenderer)
            ((DefaultTableCellRenderer) headerRenderer).setToolTipText("Hi Mom!");
        TableColumn vegetarianColumn = tableView.getColumn("Vegetarian");
        vegetarianColumn.setPreferredWidth(100);
        TableColumn numbersColumn = tableView.getColumn("Favorite Number");
        DefaultTableCellRenderer numberColumnRenderer = new DefaultTableCellRenderer() {

            public void setValue(Object value) {
                int cellValue = (value instanceof Number) ? ((Number) value).intValue() : 0;
                setForeground((cellValue > 30) ? Color.black : Color.red);
                setText((value == null) ? "" : value.toString());
            }
        };
        numberColumnRenderer.setHorizontalAlignment(JLabel.RIGHT);
        numbersColumn.setCellRenderer(numberColumnRenderer);
        numbersColumn.setPreferredWidth(110);
        JScrollPane scrollpane = new JScrollPane(tableView);
        scrollpane.setBorder(new BevelBorder(BevelBorder.LOWERED));
        scrollpane.setPreferredSize(new Dimension(430, 200));
        contentPane.add(scrollpane);
    }

    public void init() {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            initTest(getContentPane());
        });
    }
}
