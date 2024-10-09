

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.util.EventObject;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;

public class JTreeFocusTest {

    private static DefaultMutableTreeNode root;
    Robot robot;
    static boolean passed = false;
    boolean rootSelected = false;
    boolean keysTyped = false;
    private volatile Point p = null;
    private static JFrame fr;
    private static volatile JTree tree = null;

    public static void main(String[] args) throws Exception{
         new JTreeFocusTest();
    }

    void blockTillDisplayed(JComponent comp) throws Exception {
        while (p == null) {
            try {
                SwingUtilities.invokeAndWait(() -> {
                    p = comp.getLocationOnScreen();
                });
            } catch (IllegalStateException e) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                }
            }
        }
    }

    public JTreeFocusTest() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            fr = new JFrame("Test");

            root = new DefaultMutableTreeNode("root");
            JPanel p = new JPanel();
            p.setBorder(new CompoundBorder(new BevelBorder(BevelBorder.RAISED),
                    new LineBorder(UIManager.getColor("control"), 7)));
            p.setLayout(new GridLayout(2,2));
            p.add(new JLabel("one"));
            JTextField tf1  = new JTextField(10);
            p.add(tf1);
            p.add(new JLabel("two"));
            p.add(new JTextField(10));
            root.add(new DefaultMutableTreeNode(p));

            tf1.addFocusListener(new FocusAdapter() {
                public void focusGained(FocusEvent e) {
                    setPassed(true);
                }
            });

            DefaultTreeModel model = new DefaultTreeModel(root);
            tree = new JTree(model) {
                public void processKeyEvent(KeyEvent e) {
                    super.processKeyEvent(e);
                    if (e.getKeyCode()==KeyEvent.VK_F2) {
                        synchronized (JTreeFocusTest.this) {
                            keysTyped = true;
                            JTreeFocusTest.this.notifyAll();
                        }
                    }
                }
            };

            tree.addTreeSelectionListener(new TreeSelectionListener() {
                public void valueChanged(TreeSelectionEvent e) {
                    if ( root.equals(e.getPath().getLastPathComponent()) ) {
                        synchronized (JTreeFocusTest.this) {
                            rootSelected = true;
                            JTreeFocusTest.this.notifyAll();
                        }
                    }
                }
            });

            tree.setEditable(true);
            DefaultTreeCellRenderer renderer = new FormRenderer();
            tree.setCellRenderer(renderer);
            DefaultTreeCellEditor editor = new FormEditor(tree, renderer);
            tree.setCellEditor(editor);
            fr.getContentPane().add(tree);

            fr.setSize(300,400);
            fr.setVisible(true);
        });
        blockTillDisplayed(tree);
        SwingUtilities.invokeAndWait(() -> {
            tree.requestFocus();
            tree.setSelectionRow(0);
        });

        try {
            synchronized (this) {
                while (!rootSelected) {
                    JTreeFocusTest.this.wait();
                }
            }

            robot = new Robot();
            robot.setAutoDelay(50);
            robot.delay(150);
            robot.keyPress(KeyEvent.VK_DOWN);
            robot.keyRelease(KeyEvent.VK_DOWN);
            robot.keyPress(KeyEvent.VK_RIGHT);
            robot.keyRelease(KeyEvent.VK_RIGHT);
            robot.keyPress(KeyEvent.VK_F2);
            robot.keyRelease(KeyEvent.VK_F2);

            synchronized (this) {
                while (!keysTyped) {
                    JTreeFocusTest.this.wait();
                }
            }
            Thread.sleep(3000);
        } catch(Throwable t) {
            t.printStackTrace();
        }
        destroy();
    }

    public void destroy() throws Exception {
        SwingUtilities.invokeAndWait(()->fr.dispose());
        if ( !isPassed() ) {
            throw new RuntimeException("Focus wasn't transferred to the proper component");
        }
    }

    synchronized void setPassed(boolean passed) {
        this.passed = passed;
    }

    synchronized boolean isPassed() {
        return passed;
    }

    static JTree createTree() {
        return tree;
    }

    class FormRenderer extends DefaultTreeCellRenderer {
        public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                      boolean sel,
                                                      boolean expanded,
                                                      boolean leaf, int row,
                                                      boolean hasFocus) {
            Object obj = ((DefaultMutableTreeNode)value).getUserObject();
            if (obj instanceof Component){
                return (Component)((DefaultMutableTreeNode)value).getUserObject();
            }
            return super.getTreeCellRendererComponent(tree, value, sel,
                                                      expanded, leaf, row,
                                                      hasFocus);
        }
    }

    class FormEditor extends DefaultTreeCellEditor {
        public FormEditor(JTree tree, DefaultTreeCellRenderer renderer) {
            super(tree, renderer);
        }

        public Component getTreeCellEditorComponent(JTree tree, Object value,
                                                      boolean sel,
                                                      boolean expanded,
                                                      boolean leaf, int row) {
            Object obj = ((DefaultMutableTreeNode)value).getUserObject();
            if (obj instanceof Component){
                return (Component)((DefaultMutableTreeNode)value).getUserObject();
            }
            return super.getTreeCellEditorComponent(tree, value, sel,
                                                    expanded, leaf, row);
        }

        public boolean shouldSelectCell(EventObject anEvent) {
            
            return true;
        }
    }
}
