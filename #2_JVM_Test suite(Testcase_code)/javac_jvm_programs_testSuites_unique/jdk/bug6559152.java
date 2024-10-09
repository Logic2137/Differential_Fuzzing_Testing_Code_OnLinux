



import javax.swing.DefaultCellEditor;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JComboBox;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.awt.Robot;

public class bug6559152 {
    private static JFrame frame;
    private static JComboBox cb;
    private static Robot robot;
    private static Point p = null;

    public static void main(String[] args) throws Exception {
        robot = new Robot();
        robot.setAutoDelay(100);
        try {
            SwingUtilities.invokeAndWait(() -> setupUI());
            blockTillDisplayed(cb);
            robot.waitForIdle();
            robot.delay(1000);
            test();
        } finally {
            if (frame != null) {
                SwingUtilities.invokeAndWait(() -> frame.dispose());
            }
        }
    }

    static void blockTillDisplayed(JComponent comp) throws Exception {
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

    private static void setupUI() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        DefaultTableModel model = new DefaultTableModel(1, 1);
        JTable table = new JTable(model);

        cb = new JComboBox(new String[]{"one", "two", "three"});
        cb.setEditable(true);
        table.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(cb));
        frame.add(cb);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void test() throws Exception {
        robot.mouseMove(p.x, p.y);
        robot.waitForIdle();
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        robot.waitForIdle();
        testImpl();
        checkResult();
    }

    private static void testImpl() throws Exception {
        robot.keyPress(KeyEvent.VK_DOWN);
        robot.keyRelease(KeyEvent.VK_DOWN);
        robot.waitForIdle();
        robot.keyPress(KeyEvent.VK_DOWN);
        robot.keyRelease(KeyEvent.VK_DOWN);
        robot.waitForIdle();
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        robot.waitForIdle();
    }

    private static void checkResult() {
        if (cb.getSelectedItem().equals("two")) {
            System.out.println("Test passed");
        } else {
            System.out.println("Test failed");
            throw new RuntimeException("Cannot select an item " +
                    "from popup with the ENTER key.");
        }
    }
}
