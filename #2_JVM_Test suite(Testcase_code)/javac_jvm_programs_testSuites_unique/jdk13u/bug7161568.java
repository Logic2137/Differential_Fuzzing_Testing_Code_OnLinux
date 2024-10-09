import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class bug7161568 {

    private static final int N = 50;

    private static JTabbedPane tabbedPane;

    public static void main(String[] args) throws Exception {
        UIManager.put("TabbedPane.selectionFollowsFocus", Boolean.FALSE);
        Robot robot = new Robot();
        robot.setAutoDelay(50);
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                createAndShowUI();
            }
        });
        robot.waitForIdle();
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                tabbedPane.requestFocus();
            }
        });
        robot.waitForIdle();
        for (int i = 0; i < N; i++) {
            robot.keyPress(KeyEvent.VK_LEFT);
            robot.keyRelease(KeyEvent.VK_LEFT);
            robot.waitForIdle();
        }
    }

    static void createAndShowUI() {
        JFrame frame = new JFrame("Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(100, 100);
        tabbedPane = new JTabbedPane();
        for (int i = 0; i < N; i++) {
            tabbedPane.addTab("Tab: " + i, new JLabel("Test"));
        }
        tabbedPane.setSelectedIndex(0);
        frame.getContentPane().add(tabbedPane);
        frame.setVisible(true);
    }
}
