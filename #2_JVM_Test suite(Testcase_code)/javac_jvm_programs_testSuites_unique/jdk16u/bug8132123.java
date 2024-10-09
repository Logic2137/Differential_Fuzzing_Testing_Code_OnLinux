
import java.awt.Color;
import javax.swing.JApplet;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;


public class bug8132123 extends JApplet {

    @Override
    public void init() {
        SwingUtilities.invokeLater(() -> {
            JPanel left = new JPanel();
            left.setBackground(Color.GRAY);
            JPanel right = new JPanel();
            right.setBackground(Color.GRAY);
            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                    left, right);
            splitPane.setOneTouchExpandable(true);
            getContentPane().add(splitPane);
        });
    }
}
