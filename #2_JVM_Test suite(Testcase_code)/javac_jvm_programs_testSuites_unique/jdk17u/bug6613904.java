import javax.swing.GroupLayout;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class bug6613904 {

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {

            public void run() {
                GroupLayout groupLayout = new GroupLayout(new JPanel());
                try {
                    groupLayout.createParallelGroup(null);
                    throw new RuntimeException("groupLayout.createParallelGroup(null) doesn't throw IAE");
                } catch (IllegalArgumentException e) {
                }
                try {
                    groupLayout.createParallelGroup(null, true);
                    throw new RuntimeException("groupLayout.createParallelGroup(null, true) doesn't throw IAE");
                } catch (IllegalArgumentException e) {
                }
                try {
                    groupLayout.createParallelGroup(null, false);
                    throw new RuntimeException("groupLayout.createParallelGroup(null, false) doesn't throw IAE");
                } catch (IllegalArgumentException e) {
                }
            }
        });
    }
}
