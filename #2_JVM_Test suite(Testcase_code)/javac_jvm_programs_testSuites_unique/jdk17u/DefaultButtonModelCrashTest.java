import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import javax.swing.ButtonModel;
import javax.swing.DefaultButtonModel;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class DefaultButtonModelCrashTest {

    private JFrame frame = null;

    private JPanel panel;

    private volatile Point p = null;

    public static void main(String[] args) throws Exception {
        new DefaultButtonModelCrashTest();
    }

    public DefaultButtonModelCrashTest() throws Exception {
        try {
            Robot robot = new Robot();
            robot.setAutoDelay(200);
            SwingUtilities.invokeAndWait(() -> go());
            robot.waitForIdle();
            robot.keyPress(KeyEvent.VK_TAB);
            robot.keyRelease(KeyEvent.VK_TAB);
            robot.delay(100);
            robot.keyPress(KeyEvent.VK_TAB);
            robot.keyRelease(KeyEvent.VK_TAB);
        } finally {
            if (frame != null) {
                SwingUtilities.invokeAndWait(() -> frame.dispose());
            }
        }
    }

    private void go() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container contentPane = frame.getContentPane();
        ButtonModel model = new DefaultButtonModel();
        JCheckBox check = new JCheckBox("a bit broken");
        check.setModel(model);
        panel = new JPanel(new BorderLayout());
        panel.add(new JTextField("Press Tab (twice?)"), BorderLayout.NORTH);
        panel.add(check);
        contentPane.add(panel);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }
}
