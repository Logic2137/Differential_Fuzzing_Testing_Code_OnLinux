





import javax.accessibility.AccessibleComponent;
import javax.accessibility.AccessibleTable;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

public class JTableAccessibleGetLocationOnScreen {
    private static JFrame frame;
    private static JTable table;

    public static void main(String[] args) throws Exception {

        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                constructInEDT();
                try {
                    assertGetLocation();
                } finally {
                    frame.dispose();
                }
            }
        });

    }

    private static void constructInEDT() {
        String[] columnNames = { "col1", "col2", };
        Object[][] data = { { "row1, col1", "row1, col2" },
                { "row2, col1", "row2, col2" }, };

        frame = new JFrame(
                "JTable AccessibleTableHeader and AccessibleJTableCell test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        table = new JTable(data, columnNames);
        frame.add(table);
        frame.pack();
    }

    private static void assertGetLocation() {
        
        
        
        
        AccessibleTable accessibleTable = (AccessibleTable) table
                .getAccessibleContext();
        AccessibleTable header = accessibleTable.getAccessibleColumnHeader();
        AccessibleComponent accessibleComp1 = (AccessibleComponent) header
                .getAccessibleAt(0, 0);
        
        
        if (null != accessibleComp1.getLocationOnScreen()) {
            throw new RuntimeException(
                    "JTable$AccessibleJTable$AccessibleJTableHeaderCell."
                            + "getLocation() must be null");
        }

        JComponent.AccessibleJComponent accessibleJComponent =
                (JComponent.AccessibleJComponent) table.getAccessibleContext();
        AccessibleComponent accessibleComp2 = (AccessibleComponent)
                accessibleJComponent.getAccessibleChild(3);
        
        
        if (null != accessibleComp2.getLocationOnScreen()) {
            throw new RuntimeException("JTable$AccessibleJTable$"
                    + "AccessibleJTableCell.getLocation() must be null");
        }

    }
}
