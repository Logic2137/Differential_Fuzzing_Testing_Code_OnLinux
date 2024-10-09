import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.Robot;
import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.tree.DefaultTreeCellRenderer;

public class DefaultTreeCellRendererBorderTest {

    private static JFrame frame;

    private static JTree tree;

    private static DefaultTreeCellRenderer treeCellRenderer;

    private static Robot robot;

    private static Insets margin1;

    private static Insets margin2;

    public static void main(String[] args) throws Exception {
        try {
            robot = new Robot();
            robot.setAutoDelay(50);
            SwingUtilities.invokeAndWait(() -> {
                frame = new JFrame();
                tree = new JTree();
                treeCellRenderer = new DefaultTreeCellRenderer();
                tree.add(treeCellRenderer);
                frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                frame.setSize(300, 300);
                frame.setVisible(true);
                frame.setLayout(new BorderLayout());
                tree.setRootVisible(true);
                tree.setShowsRootHandles(true);
                frame.add(tree, BorderLayout.CENTER);
            });
            robot.waitForIdle();
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            UIManager.put("Tree.rendererMargins", new Insets(2, 2, 2, 2));
            SwingUtilities.invokeAndWait(() -> {
                SwingUtilities.updateComponentTreeUI(frame);
                margin1 = treeCellRenderer.getInsets();
            });
            robot.waitForIdle();
            UIManager.put("Tree.rendererMargins", null);
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
            SwingUtilities.invokeAndWait(() -> {
                SwingUtilities.updateComponentTreeUI(frame);
                margin2 = treeCellRenderer.getInsets();
            });
            robot.waitForIdle();
            if (margin1.equals(margin2)) {
                throw new RuntimeException("Test Failed : NimbusLookAndFeel " + "Border persists for MetalLookAndFeel");
            }
        } finally {
            if (frame != null) {
                SwingUtilities.invokeAndWait(frame::dispose);
            }
        }
    }
}
