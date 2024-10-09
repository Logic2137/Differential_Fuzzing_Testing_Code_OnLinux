import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class SwingFocusTransitionTest {

    private static Robot robot;

    private static JFrame frame;

    private static JTextField textField;

    private static JButton button;

    public static void main(String[] args) throws Exception {
        robot = new Robot();
        robot.setAutoDelay(50);
        try {
            SwingUtilities.invokeAndWait(new Runnable() {

                @Override
                public void run() {
                    createAndShowGUI();
                }
            });
            robot.waitForIdle();
            checkFocusOwner(textField);
            robot.keyPress(KeyEvent.VK_TAB);
            robot.keyRelease(KeyEvent.VK_TAB);
            robot.waitForIdle();
            checkFocusOwner(button);
            robot.keyPress(KeyEvent.VK_SHIFT);
            robot.keyPress(KeyEvent.VK_TAB);
            robot.keyRelease(KeyEvent.VK_TAB);
            robot.keyRelease(KeyEvent.VK_SHIFT);
            robot.waitForIdle();
            checkFocusOwner(textField);
            robot.keyPress(KeyEvent.VK_SHIFT);
            robot.keyPress(KeyEvent.VK_TAB);
            robot.keyRelease(KeyEvent.VK_TAB);
            robot.keyRelease(KeyEvent.VK_SHIFT);
            robot.waitForIdle();
            checkFocusOwner(button);
        } finally {
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    if (frame != null) {
                        frame.dispose();
                    }
                }
            });
        }
        System.out.println("Test Passed!");
    }

    private static void createAndShowGUI() {
        frame = new JFrame("SwingFocusTransitionTest");
        frame.setSize(300, 300);
        frame.setFocusTraversalPolicyProvider(true);
        frame.setFocusTraversalPolicy(new LayoutFocusTraversalPolicy());
        textField = new JTextField();
        button = new JButton();
        JPanel panel = new JPanel();
        panel.setFocusTraversalPolicyProvider(true);
        panel.setFocusTraversalPolicy(new DefaultFocusTraversalPolicy());
        JPanel p = new JPanel();
        p.setLayout(new GridLayout(3, 1));
        p.add(textField);
        p.add(button);
        p.add(panel);
        frame.add(p);
        frame.setVisible(true);
    }

    private static void checkFocusOwner(final Component component) throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                if (component != frame.getFocusOwner()) {
                    throw new RuntimeException("Test Failed! Incorrect focus owner: " + frame.getFocusOwner() + ", but expected: " + component);
                }
            }
        });
    }
}
