



import java.awt.FontMetrics;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class TestGlyphBreak {

    static JFrame f;
    static int btnHeight;
    static FontMetrics fm;

    public static void main(String[] args) throws Exception {

        SwingUtilities.invokeAndWait(() -> {

            String str = "<html><font size=2 color=red><bold>Three!</font></html>";
            JButton b = new JButton();
            b.setText(str);

            f = new JFrame();
            f.add(b);
            f.pack();
            f.setVisible(true);
            btnHeight = b.getHeight();
            fm = b.getFontMetrics(b.getFont());

        });

        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
        }
        SwingUtilities.invokeAndWait(() -> f.dispose());
        System.out.println("metrics getHeight " + fm.getHeight() +
                             " button height " + btnHeight);

        
        
        if (btnHeight > 2*fm.getHeight()) {
            throw new RuntimeException("TextView is broken into different lines");
        }
    }
}
