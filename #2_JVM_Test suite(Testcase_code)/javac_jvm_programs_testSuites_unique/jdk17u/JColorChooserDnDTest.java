import javax.swing.BorderFactory;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class JColorChooserDnDTest {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                JFrame frame = new JFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                JPanel panel = new JPanel();
                JColorChooser colorChooser = new JColorChooser();
                colorChooser.setDragEnabled(true);
                panel.setBorder(BorderFactory.createTitledBorder("JColorChoosers"));
                panel.add(colorChooser);
                frame.setContentPane(panel);
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
}
