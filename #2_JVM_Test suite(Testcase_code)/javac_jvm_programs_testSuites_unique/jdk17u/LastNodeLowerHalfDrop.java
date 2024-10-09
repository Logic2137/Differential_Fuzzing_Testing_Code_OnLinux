import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.InputEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.swing.DropMode;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class LastNodeLowerHalfDrop {

    private static DefaultMutableTreeNode b1;

    private static DefaultMutableTreeNode b2;

    private static DefaultMutableTreeNode c;

    private static JTree jTree;

    private static DefaultMutableTreeNode a;

    private static DefaultMutableTreeNode b;

    private static DefaultMutableTreeNode a1;

    private static Point dragPoint;

    private static Point dropPoint;

    private static JFrame f;

    private static DefaultMutableTreeNode c1;

    private static DefaultMutableTreeNode root;

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                f = new JFrame();
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.add(new LastNodeLowerHalfDrop().getContent());
                f.setSize(400, 400);
                f.setLocationRelativeTo(null);
                f.setVisible(true);
            }
        });
        testCase(b2, a1, +0.4f);
        if (!"b2".equals(jTree.getModel().getChild(a, a.getChildCount() - 1).toString())) {
            cleanUp();
            throw new RuntimeException("b1 was not inserted " + "in the last position in a");
        }
        testCase(c1, c, -0.4f);
        if (!"c1".equals(jTree.getModel().getChild(root, 2).toString())) {
            cleanUp();
            throw new RuntimeException("c1 was not inserted " + "between c and b nodes");
        }
        cleanUp();
    }

    private static void cleanUp() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                f.dispose();
            }
        });
    }

    private static void testCase(final DefaultMutableTreeNode drag, final DefaultMutableTreeNode drop, final float shift) throws Exception {
        Robot robot = new Robot();
        robot.waitForIdle();
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                Rectangle rectDrag = jTree.getPathBounds(new TreePath(drag.getPath()));
                dragPoint = new Point((int) rectDrag.getCenterX(), (int) rectDrag.getCenterY());
                SwingUtilities.convertPointToScreen(dragPoint, jTree);
                Rectangle rectDrop = jTree.getPathBounds(new TreePath(drop.getPath()));
                dropPoint = new Point(rectDrop.x + 5, (int) (rectDrop.getCenterY() + shift * rectDrop.height));
                SwingUtilities.convertPointToScreen(dropPoint, jTree);
            }
        });
        robot.mouseMove(dragPoint.x, dragPoint.y);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.delay(1000);
        robot.mouseMove(dropPoint.x, dropPoint.y);
        robot.delay(1000);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        robot.delay(1000);
        robot.waitForIdle();
    }

    private JScrollPane getContent() {
        jTree = new JTree(getTreeModel());
        jTree.setRootVisible(false);
        jTree.setDragEnabled(true);
        jTree.setDropMode(DropMode.INSERT);
        jTree.setTransferHandler(new TreeTransferHandler());
        jTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        expandTree(jTree);
        return new JScrollPane(jTree);
    }

    protected static TreeModel getTreeModel() {
        root = new DefaultMutableTreeNode("Root");
        a = new DefaultMutableTreeNode("A");
        root.add(a);
        a1 = new DefaultMutableTreeNode("a1");
        a.add(a1);
        b = new DefaultMutableTreeNode("B");
        root.add(b);
        b1 = new DefaultMutableTreeNode("b1");
        b.add(b1);
        b2 = new DefaultMutableTreeNode("b2");
        b.add(b2);
        c = new DefaultMutableTreeNode("C");
        root.add(c);
        c1 = new DefaultMutableTreeNode("c1");
        c.add(c1);
        return new DefaultTreeModel(root);
    }

    private void expandTree(JTree tree) {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();
        Enumeration e = root.breadthFirstEnumeration();
        while (e.hasMoreElements()) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
            if (node.isLeaf()) {
                continue;
            }
            int row = tree.getRowForPath(new TreePath(node.getPath()));
            tree.expandRow(row);
        }
    }
}

class TreeTransferHandler extends TransferHandler {

    DataFlavor nodesFlavor;

    DataFlavor[] flavors = new DataFlavor[1];

    DefaultMutableTreeNode[] nodesToRemove;

    public TreeTransferHandler() {
        try {
            String mimeType = DataFlavor.javaJVMLocalObjectMimeType + ";class=\"" + javax.swing.tree.DefaultMutableTreeNode[].class.getName() + "\"";
            nodesFlavor = new DataFlavor(mimeType);
            flavors[0] = nodesFlavor;
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFound: " + e.getMessage());
        }
    }

    @Override
    public boolean canImport(TransferHandler.TransferSupport support) {
        if (!support.isDrop()) {
            return false;
        }
        support.setShowDropLocation(true);
        if (!support.isDataFlavorSupported(nodesFlavor)) {
            return false;
        }
        JTree.DropLocation dl = (JTree.DropLocation) support.getDropLocation();
        JTree tree = (JTree) support.getComponent();
        int dropRow = tree.getRowForPath(dl.getPath());
        int[] selRows = tree.getSelectionRows();
        for (int i = 0; i < selRows.length; i++) {
            if (selRows[i] == dropRow) {
                return false;
            }
        }
        int action = support.getDropAction();
        if (action == MOVE) {
            return haveCompleteNode(tree);
        }
        TreePath dest = dl.getPath();
        DefaultMutableTreeNode target = (DefaultMutableTreeNode) dest.getLastPathComponent();
        TreePath path = tree.getPathForRow(selRows[0]);
        DefaultMutableTreeNode firstNode = (DefaultMutableTreeNode) path.getLastPathComponent();
        if (firstNode.getChildCount() > 0 && target.getLevel() < firstNode.getLevel()) {
            return false;
        }
        return true;
    }

    private boolean haveCompleteNode(JTree tree) {
        int[] selRows = tree.getSelectionRows();
        TreePath path = tree.getPathForRow(selRows[0]);
        DefaultMutableTreeNode first = (DefaultMutableTreeNode) path.getLastPathComponent();
        int childCount = first.getChildCount();
        if (childCount > 0 && selRows.length == 1) {
            return false;
        }
        for (int i = 1; i < selRows.length; i++) {
            path = tree.getPathForRow(selRows[i]);
            DefaultMutableTreeNode next = (DefaultMutableTreeNode) path.getLastPathComponent();
            if (first.isNodeChild(next)) {
                if (childCount > selRows.length - 1) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected Transferable createTransferable(JComponent c) {
        JTree tree = (JTree) c;
        TreePath[] paths = tree.getSelectionPaths();
        if (paths != null) {
            List<DefaultMutableTreeNode> copies = new ArrayList<>();
            List<DefaultMutableTreeNode> toRemove = new ArrayList<>();
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) paths[0].getLastPathComponent();
            DefaultMutableTreeNode copy = copy(node);
            copies.add(copy);
            toRemove.add(node);
            for (int i = 1; i < paths.length; i++) {
                DefaultMutableTreeNode next = (DefaultMutableTreeNode) paths[i].getLastPathComponent();
                if (next.getLevel() < node.getLevel()) {
                    break;
                } else if (next.getLevel() > node.getLevel()) {
                    copy.add(copy(next));
                } else {
                    copies.add(copy(next));
                    toRemove.add(next);
                }
            }
            DefaultMutableTreeNode[] nodes = copies.toArray(new DefaultMutableTreeNode[copies.size()]);
            nodesToRemove = toRemove.toArray(new DefaultMutableTreeNode[toRemove.size()]);
            return new NodesTransferable(nodes);
        }
        return null;
    }

    private DefaultMutableTreeNode copy(TreeNode node) {
        return new DefaultMutableTreeNode(node);
    }

    @Override
    protected void exportDone(JComponent source, Transferable data, int action) {
        if ((action & MOVE) == MOVE) {
            JTree tree = (JTree) source;
            DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
            for (DefaultMutableTreeNode nodesToRemove1 : nodesToRemove) {
                model.removeNodeFromParent(nodesToRemove1);
            }
        }
    }

    @Override
    public int getSourceActions(JComponent c) {
        return COPY_OR_MOVE;
    }

    @Override
    public boolean importData(TransferHandler.TransferSupport support) {
        if (!canImport(support)) {
            return false;
        }
        DefaultMutableTreeNode[] nodes = null;
        try {
            Transferable t = support.getTransferable();
            nodes = (DefaultMutableTreeNode[]) t.getTransferData(nodesFlavor);
        } catch (UnsupportedFlavorException ufe) {
            System.out.println("UnsupportedFlavor: " + ufe.getMessage());
        } catch (java.io.IOException ioe) {
            System.out.println("I/O error: " + ioe.getMessage());
        }
        JTree.DropLocation dl = (JTree.DropLocation) support.getDropLocation();
        int childIndex = dl.getChildIndex();
        TreePath dest = dl.getPath();
        DefaultMutableTreeNode parent = (DefaultMutableTreeNode) dest.getLastPathComponent();
        JTree tree = (JTree) support.getComponent();
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        int index = childIndex;
        if (childIndex == -1) {
            index = parent.getChildCount();
        }
        for (DefaultMutableTreeNode node : nodes) {
            model.insertNodeInto(node, parent, index++);
        }
        return true;
    }

    @Override
    public String toString() {
        return getClass().getName();
    }

    public class NodesTransferable implements Transferable {

        DefaultMutableTreeNode[] nodes;

        public NodesTransferable(DefaultMutableTreeNode[] nodes) {
            this.nodes = nodes;
        }

        @Override
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
            if (!isDataFlavorSupported(flavor)) {
                throw new UnsupportedFlavorException(flavor);
            }
            return nodes;
        }

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return flavors;
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return nodesFlavor.equals(flavor);
        }
    }
}
