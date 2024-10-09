

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;



public class Test8013571 extends DefaultTreeModel {
    public static void main(String[] args) {
        DefaultMutableTreeNode root = create("root");
        root.add(create("colors", "blue", "violet", "red", "yellow"));
        root.add(create("sports", "basketball", "soccer", "football", "hockey"));
        root.add(create("food", "hot dogs", "pizza", "ravioli", "bananas"));
        Test8013571 model = new Test8013571(root);
        JTree tree = new JTree(model);
        model.fireTreeChanged(tree);
    }

    private static DefaultMutableTreeNode create(String name, String... values) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(name);
        for (String value : values) {
            node.add(create(value));
        }
        return node;
    }

    private Test8013571(DefaultMutableTreeNode root) {
        super(root);
    }

    private void fireTreeChanged(Object source) {
        fireTreeNodesInserted(source, null, null, null);
        fireTreeNodesChanged(source, null, null, null);
        fireTreeNodesRemoved(source, null, null, null);
        fireTreeStructureChanged(source, null, null, null);
    }
}
