import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

public class bug6544309 {

    private JDialog dialog;

    private boolean passed;

    private static Robot robot;

    public static void main(String[] args) throws Exception {
        robot = new Robot();
        robot.setAutoDelay(100);
        robot.mouseMove(100, 100);
        robot.waitForIdle();
        final bug6544309 test = new bug6544309();
        try {
            SwingUtilities.invokeAndWait(new Runnable() {

                public void run() {
                    test.setupUI();
                }
            });
            robot.waitForIdle();
            robot.delay(1000);
            test.test();
            System.out.println("Test passed");
        } finally {
            if (test.dialog != null) {
                test.dialog.dispose();
            }
        }
    }

    private void setupUI() {
        dialog = new JDialog();
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setSize(200, 100);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        JPopupMenu popup = new JPopupMenu();
        popup.add(new JMenuItem("one"));
        JMenuItem two = new JMenuItem("two");
        two.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                passed = true;
            }
        });
        popup.add(two);
        popup.add(new JMenuItem("three"));
        popup.show(dialog, 50, 50);
    }

    private void test() throws Exception {
        testImpl();
        checkResult();
    }

    private void testImpl() throws Exception {
        robot.waitForIdle();
        System.out.println("Pressing DOWN ARROW");
        robot.keyPress(KeyEvent.VK_DOWN);
        robot.keyRelease(KeyEvent.VK_DOWN);
        robot.waitForIdle();
        System.out.println("Pressing DOWN ARROW");
        robot.keyPress(KeyEvent.VK_DOWN);
        robot.keyRelease(KeyEvent.VK_DOWN);
        robot.waitForIdle();
        System.out.println("Pressing SPACE");
        robot.keyPress(KeyEvent.VK_SPACE);
        robot.keyRelease(KeyEvent.VK_SPACE);
    }

    private void checkResult() {
        robot.waitForIdle();
        if (!passed) {
            throw new RuntimeException("If a JDialog is invoker for JPopupMenu, " + "the menu cannot be handled by keyboard.");
        }
    }
}
