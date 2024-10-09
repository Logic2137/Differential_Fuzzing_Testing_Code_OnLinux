import javax.swing.JApplet;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class bug4222153 extends JApplet {

    public void init() {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            getContentPane().add(new JTable(2, 2));
        });
    }
}
