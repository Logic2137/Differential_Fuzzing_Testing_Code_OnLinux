


import java.beans.PropertyVetoException;
import javax.swing.JFrame;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.Robot;
import javax.swing.SwingUtilities;

public class TestJInternalFrameIconify {

    private static JDesktopPane desktopPane;
    private static JFrame frame;
    private static Robot robot;
    private static volatile String errorMessage = "";

    public static void main(String[] args) throws Exception {
        robot = new java.awt.Robot();
        UIManager.LookAndFeelInfo[] lookAndFeelArray
                = UIManager.getInstalledLookAndFeels();
        for (UIManager.LookAndFeelInfo lookAndFeelItem : lookAndFeelArray) {
            String lookAndFeelString = lookAndFeelItem.getClassName();
            if (tryLookAndFeel(lookAndFeelString)) {
                createUI(lookAndFeelString);
                robot.waitForIdle();
                executeTest(lookAndFeelString);
            }
        }
        if (!"".equals(errorMessage)) {
            throw new RuntimeException(errorMessage);
        }
    }

    private static boolean tryLookAndFeel(String lookAndFeelString) {
        try {
            UIManager.setLookAndFeel(lookAndFeelString);
            return true;
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException |
                InstantiationException | IllegalAccessException e) {
            errorMessage += e.getMessage() + "\n";
            System.err.println("Caught Exception: " + e.getMessage());
            return false;
        }
    }

    private static void createUI(String lookAndFeelString) throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                frame = new JFrame(lookAndFeelString);
                desktopPane = new JDesktopPane();
                frame.getContentPane().add(desktopPane);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                JInternalFrame f = new JInternalFrame("Child ", true, true,
                        true, true);
                f.setSize(200, 300);
                f.setLocation(20, 20);
                try {
                    f.setIcon(true);
                } catch (PropertyVetoException ex) {
                    errorMessage += ex.getMessage() + "\n";
                }
                desktopPane.add(f);
                f.setVisible(true);

                frame.setSize(500, 500);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });

    }

    private static void executeTest(String lookAndFeelString) throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                try {
                    JInternalFrame internalFrames[]
                            = desktopPane.getAllFrames();
                    if (internalFrames[0].isShowing()) {
                        errorMessage += "Test Failed for "
                                + lookAndFeelString + " look and feel\n";
                        System.err.println(errorMessage);
                    }
                } finally {
                    frame.dispose();
                }
            }
        });
    }
}
