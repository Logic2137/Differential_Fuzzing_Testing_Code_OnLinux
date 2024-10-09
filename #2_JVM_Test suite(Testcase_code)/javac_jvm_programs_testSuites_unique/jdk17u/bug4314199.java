import javax.swing.*;
import javax.swing.tree.*;

public class bug4314199 extends JApplet {

    public void init() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
            SwingUtilities.invokeAndWait(new Runnable() {

                public void run() {
                    createAndShowGUI();
                }
            });
        } catch (final Exception e) {
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    createAndShowMessage("Test fails because of exception: " + e.getMessage());
                }
            });
        }
    }

    private void createAndShowMessage(String message) {
        getContentPane().add(new JLabel(message));
    }

    private void createAndShowGUI() {
        JMenuBar mb = new JMenuBar();
        mb.add(Box.createHorizontalStrut(27));
        JMenu mn = new JMenu("Menu");
        JMenuItem mi = new JMenuItem("MenuItem");
        mn.add(mi);
        mb.add(mn);
        setJMenuBar(mb);
        DefaultMutableTreeNode n1 = new DefaultMutableTreeNode("Root");
        DefaultMutableTreeNode n2 = new DefaultMutableTreeNode("Duke");
        n1.add(n2);
        DefaultMutableTreeNode n3 = new DefaultMutableTreeNode("Bug");
        n2.add(n3);
        n3.add(new DefaultMutableTreeNode("Blah"));
        n3.add(new DefaultMutableTreeNode("Blah"));
        n3.add(new DefaultMutableTreeNode("Blah"));
        DefaultMutableTreeNode n4 = new DefaultMutableTreeNode("Here");
        n2.add(n4);
        JTree tree = new JTree(new DefaultTreeModel(n1));
        tree.putClientProperty("JTree.lineStyle", "Angled");
        tree.expandPath(new TreePath(new Object[] { n1, n2, n3 }));
        setContentPane(tree);
    }
}
