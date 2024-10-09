import java.awt.Graphics;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.CellRendererPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class TestMemLeakComboBox {

    private static JFrame frame;

    private static String failed = null;

    private static void setLookAndFeel(UIManager.LookAndFeelInfo laf) {
        try {
            UIManager.setLookAndFeel(laf.getClassName());
        } catch (UnsupportedLookAndFeelException ignored) {
            System.out.println("Unsupported L&F: " + laf.getClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static class MyPanel extends JPanel {

        public void paint(Graphics g) {
            super.paint(g);
            for (Component child : getComponents()) {
                verifyChild(child);
            }
        }

        private void verifyChild(Component c) {
            if (c instanceof JComboBox) {
                for (Component child : ((Container) c).getComponents()) {
                    if (child instanceof CellRendererPane && ((CellRendererPane) child).getComponentCount() > 0) {
                        failed = new String("CellRendererPane still has children for: " + c);
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        for (UIManager.LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels()) {
            System.out.println("Testing l&f : " + laf.getClassName());
            SwingUtilities.invokeAndWait(() -> setLookAndFeel(laf));
            test();
            if (failed != null) {
                throw new RuntimeException(failed);
            }
        }
    }

    private static void test() throws Exception {
        try {
            SwingUtilities.invokeAndWait(() -> {
                frame = new JFrame();
                JPanel panel = new MyPanel();
                panel.setPreferredSize(new Dimension(100, 100));
                panel.setLayout(new FlowLayout());
                panel.add(new JComboBox(new String[] { "one", "two", "three" }));
                frame.add(panel);
                frame.pack();
                frame.setVisible(true);
            });
        } finally {
            if (frame != null) {
                SwingUtilities.invokeAndWait(() -> frame.dispose());
            }
        }
    }
}
