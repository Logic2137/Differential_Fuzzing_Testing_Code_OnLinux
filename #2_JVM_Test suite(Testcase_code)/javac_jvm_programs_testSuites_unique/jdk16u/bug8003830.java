

import java.awt.EventQueue;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import javax.swing.JTree;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.TreePath;




public class bug8003830 implements Runnable {
    public static void main(String[] args) throws Exception {
        EventQueue.invokeAndWait(new bug8003830());
    }
    @Override
    public void run() {
        testNPEAtActionsPage();
    }

    public void testNPEAtActionsPage() {
        JTree tree = new JTree();
        BasicTreeUI ui = new NullReturningTreeUI();
        tree.setUI(ui);
        BasicTreeUI.TreePageAction tpa = ui.new TreePageAction(0, "down");
        tpa.actionPerformed(new ActionEvent(tree, 0, ""));
    }

    private static final class NullReturningTreeUI extends BasicTreeUI {
        @Override
        public Rectangle getPathBounds(JTree tree, TreePath path) {
            
            
            return null;
        }
    }
}
