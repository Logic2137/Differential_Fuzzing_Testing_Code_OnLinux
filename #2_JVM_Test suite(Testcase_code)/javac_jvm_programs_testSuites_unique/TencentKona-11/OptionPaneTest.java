


import java.awt.Component;
import java.awt.Insets;
import java.awt.Robot;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class OptionPaneTest {

    private volatile static boolean testFailed;
    private static JDialog dialog;
    private static Robot robot;

    public static void main(final String[] args) throws Exception {
        robot = new Robot();
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                try {
                    JOptionPane optionPane = new JOptionPane("JOptionPane",
                            JOptionPane.INFORMATION_MESSAGE,
                            JOptionPane.DEFAULT_OPTION,
                            null,
                            new String[]{"3", "2", "1"},
                            null);
                    dialog = optionPane.createDialog("JOptionPane");
                    int width = 0;
                    Component[] comps = optionPane.getComponents();
                    for (Component comp : comps) {
                        if (comp instanceof JPanel) {
                            Component[] child = ((JPanel) comp).getComponents();
                            for (Component c : child) {
                                if (c instanceof JButton) {
                                    width += c.getWidth();
                                }
                            }
                        }
                    }
                    Insets in = optionPane.getInsets();
                    width += in.left + in.right;
                    if (width > optionPane.getWidth()) {
                        testFailed = true;
                    }
                } finally {
                    dialog.dispose();
                }
            }
        });
        robot.waitForIdle();
        if (testFailed) {
            throw new RuntimeException("Test Failed");
        }
    }
}
