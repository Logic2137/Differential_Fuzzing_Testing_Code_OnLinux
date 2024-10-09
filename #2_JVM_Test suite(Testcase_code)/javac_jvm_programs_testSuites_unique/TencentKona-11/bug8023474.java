



import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;
import java.awt.event.InputEvent;
import java.util.EventObject;

public class bug8023474 {
    private static JTree tree;

    public static void main(String[] args) throws Exception {
        Robot robot = new Robot();
        robot.setAutoDelay(50);

        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });

        robot.waitForIdle();

        Point point = getRowPointToClick(1);
        robot.mouseMove(point.x, point.y);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);

        robot.waitForIdle();

        Boolean result = (Boolean)tree.getCellEditor().getCellEditorValue();
        if (!result) {
            throw new RuntimeException("Test Failed!");
        }
    }

    private static void createAndShowGUI() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
        DefaultMutableTreeNode item = new DefaultMutableTreeNode("item");
        DefaultMutableTreeNode subItem = new DefaultMutableTreeNode("subItem");

        root.add(item);
        item.add(subItem);

        DefaultTreeModel model = new DefaultTreeModel(root);
        tree = new JTree(model);

        tree.setCellEditor(new Editor());
        tree.setEditable(true);
        tree.setRowHeight(30);
        tree.setCellRenderer(new CheckboxCellRenderer());

        JFrame frame = new JFrame("bug8023474");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new JScrollPane(tree));
        frame.setSize(400, 300);
        frame.setVisible(true);
    }

    private static Point getRowPointToClick(final int row) throws Exception {
        final Point[] result = new Point[1];

        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                Rectangle rect = tree.getRowBounds(row);
                Point point = new Point(rect.x + 10, rect.y + rect.height / 2);
                SwingUtilities.convertPointToScreen(point, tree);
                result[0] = point;
            }
        });
        return result[0];
    }

    private static class Editor extends JPanel implements TreeCellEditor {
        private JCheckBox checkbox;

        public Editor() {
            setOpaque(false);
            checkbox = new JCheckBox();
            add(checkbox);
        }

        public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected,
                                                    boolean expanded, boolean leaf, int row) {
            checkbox.setText(value.toString());
            checkbox.setSelected(false);
            return this;
        }

        public Object getCellEditorValue() {
            return checkbox.isSelected();
        }

        public boolean isCellEditable(EventObject anEvent) {
            return true;
        }

        public boolean shouldSelectCell(EventObject anEvent) {
            return true;
        }

        public boolean stopCellEditing() {
            return true;
        }

        public void cancelCellEditing() {
        }

        public void addCellEditorListener(CellEditorListener l) {
        }

        public void removeCellEditorListener(CellEditorListener l) {
        }
    }

    private static class CheckboxCellRenderer extends JPanel implements TreeCellRenderer {
        private JCheckBox checkbox;

        public CheckboxCellRenderer() {
            setOpaque(false);
            checkbox = new JCheckBox();
            add(checkbox);
        }

        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
                                                      boolean leaf, int row, boolean hasFocus) {
            checkbox.setText(value.toString());
            checkbox.setSelected(false);
            return this;
        }
    }
}
