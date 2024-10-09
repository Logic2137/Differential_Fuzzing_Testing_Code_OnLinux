import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

public class Test8015926 implements TreeModelListener, Runnable, Thread.UncaughtExceptionHandler {

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        SwingUtilities.invokeAndWait(new Test8015926());
        Thread.sleep(1000L);
    }

    private JTree tree;

    @Override
    public void treeStructureChanged(TreeModelEvent event) {
    }

    @Override
    public void treeNodesRemoved(TreeModelEvent event) {
    }

    @Override
    public void treeNodesInserted(TreeModelEvent event) {
        this.tree.expandPath(event.getTreePath());
    }

    @Override
    public void treeNodesChanged(TreeModelEvent event) {
    }

    @Override
    public void run() {
        Thread.currentThread().setUncaughtExceptionHandler(this);
        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
        DefaultMutableTreeNode child = new DefaultMutableTreeNode("Child");
        DefaultTreeModel model = new DefaultTreeModel(root);
        this.tree = new JTree();
        this.tree.setModel(model);
        JFrame frame = new JFrame(getClass().getSimpleName());
        frame.add(this.tree);
        model.addTreeModelListener(this);
        model.insertNodeInto(child, root, root.getChildCount());
        model.removeNodeFromParent(child);
        frame.setSize(640, 480);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable exception) {
        exception.printStackTrace();
        System.exit(1);
    }
}
