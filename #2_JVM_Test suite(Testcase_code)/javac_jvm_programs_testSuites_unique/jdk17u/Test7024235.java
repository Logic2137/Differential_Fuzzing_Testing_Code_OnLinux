import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Rectangle;
import java.awt.Robot;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class Test7024235 implements Runnable {

    private static final boolean AUTO = null != System.getProperty("test.src", null);

    public static void main(String[] args) throws Exception {
        Test7024235 test = new Test7024235();
        for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            String className = info.getClassName();
            System.out.println("className = " + className);
            UIManager.setLookAndFeel(className);
            test.test();
            try {
                Robot robot = new Robot();
                robot.waitForIdle();
                robot.delay(1000);
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new Error("Unexpected Failure");
            }
            test.test();
        }
    }

    private volatile JTabbedPane pane;

    private volatile boolean passed;

    public void run() {
        if (this.pane == null) {
            this.pane = new JTabbedPane();
            this.pane.addTab("1", new Container());
            this.pane.addTab("2", new JButton());
            this.pane.addTab("3", new JCheckBox());
            JFrame frame = new JFrame();
            frame.add(BorderLayout.WEST, this.pane);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            test("first");
        } else {
            test("second");
            if (this.passed || AUTO) {
                SwingUtilities.getWindowAncestor(this.pane).dispose();
            }
            this.pane = null;
        }
    }

    private void test() throws Exception {
        SwingUtilities.invokeAndWait(this);
        if (!this.passed && AUTO) {
            throw new Error("TEST FAILED");
        }
    }

    private void test(String step) {
        this.passed = true;
        for (int index = 0; index < this.pane.getTabCount(); index++) {
            Rectangle bounds = this.pane.getBoundsAt(index);
            if (bounds == null) {
                continue;
            }
            int centerX = bounds.x + bounds.width / 2;
            int centerY = bounds.y + bounds.height / 2;
            int actual = this.pane.indexAtLocation(centerX, centerY);
            if (index != actual) {
                System.out.println("name = " + UIManager.getLookAndFeel().getName());
                System.out.println("step = " + step);
                System.out.println("index = " + index);
                System.out.println("bounds = " + bounds);
                System.out.println("indexAtLocation(" + centerX + "," + centerX + ") returns " + actual);
                this.passed = false;
            }
        }
    }
}
