



import java.awt.Component;
import java.awt.event.InputEvent;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;

public class Test8003400 {

    private static final String TITLE = "Test JTree with a large model";
    private static List<String> OBJECTS = Arrays.asList(TITLE, "x", "y", "z");
    private static JScrollPane pane;
    private static JFrame frame;
    private static JTree tree;
    private static Point point;
    private static Rectangle rect;

    public static void blockTillDisplayed(Component comp) {
        Point p = null;
        while (p == null) {
            try {
                p = comp.getLocationOnScreen();
            } catch (IllegalStateException e) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ie) {
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        for (String arg : args) {
            if (arg.equals("reverse")) {
                Collections.reverse(OBJECTS);
            }
        }
        UIManager.LookAndFeelInfo infos[] = UIManager.getInstalledLookAndFeels();
        for (UIManager.LookAndFeelInfo info : infos) {
            UIManager.setLookAndFeel(info.getClassName());
            System.out.println(info.getClassName());
            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    public void run() {
                        DefaultMutableTreeNode root = new DefaultMutableTreeNode();

                        tree = new JTree(root);
                        tree.setLargeModel(true);
                        tree.setRowHeight(16);

                        frame = new JFrame(TITLE);
                        frame.add(pane = new JScrollPane(tree));
                        frame.setSize(200, 100);
                        frame.setAlwaysOnTop(true);
                        frame.setLocationRelativeTo(null);
                        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        frame.setVisible(true);

                        for (String object : OBJECTS) {
                            root.add(new DefaultMutableTreeNode(object));
                        }
                        tree.expandRow(0);
                    }
                });

                blockTillDisplayed(frame);

                Robot robot = new Robot();
                robot.setAutoDelay(100);
                SwingUtilities.invokeAndWait(() -> {
                    point = tree.getLocationOnScreen();
                    rect = tree.getBounds();
                });
                robot.waitForIdle();
                robot.delay(500);
                robot.mouseMove(point.x + rect.width / 2, point.y + rect.height / 3);
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

                robot.waitForIdle();
                robot.delay(1000);
                robot.keyPress(KeyEvent.VK_END);
                robot.keyRelease(KeyEvent.VK_END);
                robot.waitForIdle();
                robot.delay(1000);

                SwingUtilities.invokeAndWait(new Runnable() {
                    public void run() {
                        JTree tree = (JTree) pane.getViewport().getView();
                        Rectangle inner = tree.getRowBounds(tree.getRowCount() - 1);
                        Rectangle outer = SwingUtilities.convertRectangle(tree, inner, pane);
                        int heightDifference = outer.y + tree.getRowHeight() - pane.getVerticalScrollBar().getHeight();
                        
                        if (null != System.getProperty("test.src", null)) {
                            frame.dispose();
                            if (heightDifference > 3) {
                                throw new Error("TEST FAILED: " + heightDifference);
                            }
                        }
                    }
                });
            } finally {
                if (frame != null) {
                    SwingUtilities.invokeAndWait(frame::dispose);
                }
            }
        }
    }
}
