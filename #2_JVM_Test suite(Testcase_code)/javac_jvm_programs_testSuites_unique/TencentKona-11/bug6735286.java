



import javax.swing.JTable;
import javax.swing.SwingUtilities;

public class bug6735286 {
    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                new JTable().getDefaultRenderer(Object.class).getTableCellRendererComponent(null, "a value",
                        true, true, 0, 0);

            }
        });
    }
}
