



import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class bug4235420 {

    public static void main(String[] argv) throws Exception {
        if ("Nimbus".equals(UIManager.getLookAndFeel().getName())) {
            System.out.println("The test is skipped for Nimbus");

            return;
        }

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                Table table = new Table();

                table.test();
            }
        });
    }

    private static class Table extends JTable {
        public void test() {
            
            Class[] rendererClasses = {Object.class, Number.class, Date.class, ImageIcon.class, Boolean.class};

            Map copy = new HashMap(defaultRenderersByColumnClass);

            for (Class rendererClass : rendererClasses) {
                Object obj = copy.get(rendererClass);

                if (obj instanceof TableCellRenderer) {
                    throw new Error("Failed: TableCellRenderer created for " +
                            rendererClass.getClass().getName());
                }
            }

            
            Class[] editorClasses = {Object.class, Number.class, Boolean.class};

            copy = new HashMap(defaultEditorsByColumnClass);

            for (Class editorClass : editorClasses) {
                Object obj = copy.get(editorClass);

                if (obj instanceof TableCellEditor) {
                    throw new Error("Failed: TableCellEditor created for " +
                            editorClass.getClass().getName());
                }
            }
        }
    }
}
