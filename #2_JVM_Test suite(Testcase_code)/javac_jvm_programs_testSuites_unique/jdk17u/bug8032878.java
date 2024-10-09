import java.awt.*;
import java.awt.event.KeyEvent;
import javax.swing.*;
import javax.swing.text.JTextComponent;
import javax.swing.plaf.metal.MetalLookAndFeel;

public class bug8032878 implements Runnable {

    private static final String ONE = "one";

    private static final String TWO = "two";

    private static final String THREE = "three";

    private static final String EXPECTED = "one123";

    private final Robot robot;

    private JFrame frame;

    private JComboBox cb;

    private volatile boolean surrender;

    private volatile String text;

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(new MetalLookAndFeel());
        final bug8032878 test = new bug8032878();
        test.test(false);
        test.test(true);
    }

    public bug8032878() throws AWTException {
        robot = new Robot();
        robot.setAutoDelay(100);
    }

    private void setupUI() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JTable table = new JTable(new String[][] { { ONE }, { TWO }, { THREE } }, new String[] { "#" });
        table.setSurrendersFocusOnKeystroke(surrender);
        cb = new JComboBox(new String[] { ONE, TWO, THREE });
        cb.setEditable(true);
        DefaultCellEditor comboEditor = new DefaultCellEditor(cb);
        comboEditor.setClickCountToStart(1);
        table.getColumnModel().getColumn(0).setCellEditor(comboEditor);
        frame.add(table);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    private void test(final boolean flag) throws Exception {
        try {
            surrender = flag;
            SwingUtilities.invokeAndWait(this);
            robot.waitForIdle();
            robot.delay(1000);
            runTest();
            checkResult();
        } finally {
            if (frame != null) {
                SwingUtilities.invokeAndWait(() -> frame.dispose());
            }
        }
    }

    private void runTest() throws Exception {
        robot.waitForIdle();
        robot.keyPress(KeyEvent.VK_TAB);
        robot.keyRelease(KeyEvent.VK_TAB);
        robot.waitForIdle();
        robot.keyPress(KeyEvent.VK_1);
        robot.keyRelease(KeyEvent.VK_1);
        robot.waitForIdle();
        robot.keyPress(KeyEvent.VK_2);
        robot.keyRelease(KeyEvent.VK_2);
        robot.waitForIdle();
        robot.keyPress(KeyEvent.VK_3);
        robot.keyRelease(KeyEvent.VK_3);
        robot.waitForIdle();
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        robot.waitForIdle();
    }

    private void checkResult() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {

            public void run() {
                text = ((JTextComponent) cb.getEditor().getEditorComponent()).getText();
            }
        });
        if (text.equals(EXPECTED)) {
            System.out.println("Test with surrender = " + surrender + " passed");
        } else {
            System.out.println("Test with surrender = " + surrender + " failed");
            throw new RuntimeException("Expected value in JComboBox editor '" + EXPECTED + "' but found '" + text + "'.");
        }
    }

    @Override
    public void run() {
        setupUI();
    }
}
