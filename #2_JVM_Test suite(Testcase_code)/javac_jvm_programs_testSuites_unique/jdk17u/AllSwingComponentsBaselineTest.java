import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicButtonUI;

public class AllSwingComponentsBaselineTest {

    public static void main(String[] args) throws Exception {
        for (UIManager.LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels()) {
            System.out.println("Test for LookAndFeel " + laf.getClassName());
            UIManager.setLookAndFeel(laf.getClassName());
            SwingUtilities.invokeAndWait(() -> test());
            System.out.println("Test passed for LookAndFeel " + laf.getClassName());
        }
    }

    public static void test() {
        JFrame frame = null;
        try {
            frame = new JFrame();
            JButton b = new JButton("test");
            FlowLayout layout = new FlowLayout();
            layout.setAlignOnBaseline(true);
            frame.getContentPane().setLayout(layout);
            frame.getContentPane().add(b);
            BasicButtonUI bbUI = (BasicButtonUI) UIManager.getUI(b);
            bbUI.getBaseline(b, b.getHeight(), b.getWidth());
        } finally {
            if (frame != null) {
                frame.dispose();
            }
        }
    }
}
