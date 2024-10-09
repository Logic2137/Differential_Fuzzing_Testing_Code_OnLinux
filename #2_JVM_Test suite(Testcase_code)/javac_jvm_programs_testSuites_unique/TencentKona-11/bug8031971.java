
import java.util.Date;
import java.util.Hashtable;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.SwingUtilities;


public class bug8031971 {

    static Object[][] RENDERERS = {
        {Object.class, "javax.swing.table.DefaultTableCellRenderer$UIResource"},
        {Number.class, "javax.swing.JTable$NumberRenderer"},
        {Float.class, "javax.swing.JTable$DoubleRenderer"},
        {Double.class, "javax.swing.JTable$DoubleRenderer"},
        {Date.class, "javax.swing.JTable$DateRenderer"},
        {Icon.class, "javax.swing.JTable$IconRenderer"},
        {ImageIcon.class, "javax.swing.JTable$IconRenderer"},
        {Boolean.class, "javax.swing.JTable$BooleanRenderer"}
    };

    static Object[][] EDITORS = {
        {Object.class, "javax.swing.JTable$GenericEditor"},
        {Number.class, "javax.swing.JTable$NumberEditor"},
        {Boolean.class, "javax.swing.JTable$BooleanEditor"}
    };

    public static void main(String[] args) throws Exception {

        SwingUtilities.invokeAndWait(() -> {

            TestTable table = new TestTable();
            test(table.getDefaultRenderersByColumnClass(), RENDERERS);
            test(table.getDefaultEditorsByColumnClass(), EDITORS);
        });
    }

    static void test(Hashtable table, Object[][] values) {
        for (int i = 0; i < values.length; i++) {
            test(table.get(values[i][0]), (String) values[i][1]);
        }
    }

    static void test(Object obj, String className) {
        if (!obj.getClass().getCanonicalName().equals(className.replace('$', '.'))) {
            throw new RuntimeException("Wrong value!");
        }
    }

    static class TestTable extends JTable {

        Hashtable getDefaultRenderersByColumnClass() {
            return defaultRenderersByColumnClass;
        }

        Hashtable getDefaultEditorsByColumnClass() {
            return defaultEditorsByColumnClass;
        }
    }
}
