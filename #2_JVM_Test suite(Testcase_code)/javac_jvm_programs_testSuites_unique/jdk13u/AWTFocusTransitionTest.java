import java.awt.*;
import java.awt.event.KeyEvent;

public class AWTFocusTransitionTest {

    private static Robot robot;

    private static Frame frame;

    private static TextField textField;

    private static Button button;

    public static void main(String[] args) throws Exception {
        robot = new Robot();
        robot.setAutoDelay(50);
        try {
            createAndShowGUI();
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
            if (frame != null) {
                frame.dispose();
            }
        }
        System.out.println("Test Passed!");
    }

    private static void createAndShowGUI() {
        frame = new Frame("AWTFocusTransitionTest");
        frame.setSize(300, 300);
        frame.setFocusTraversalPolicyProvider(true);
        frame.setFocusTraversalPolicy(new DefaultFocusTraversalPolicy());
        textField = new TextField();
        button = new Button();
        Panel panel = new Panel();
        panel.setFocusTraversalPolicyProvider(true);
        panel.setFocusTraversalPolicy(new DefaultFocusTraversalPolicy());
        Panel p = new Panel();
        p.setLayout(new GridLayout(3, 1));
        p.add(textField);
        p.add(button);
        p.add(panel);
        frame.add(p);
        frame.setVisible(true);
    }

    private static void checkFocusOwner(Component component) {
        if (component != frame.getFocusOwner()) {
            throw new RuntimeException("Test Failed! Incorrect focus owner: " + frame.getFocusOwner() + ", but expected: " + component);
        }
    }
}
