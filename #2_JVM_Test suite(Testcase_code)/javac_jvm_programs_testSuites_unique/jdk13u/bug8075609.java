import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;

public class bug8075609 {

    private static Robot robot;

    private static JTextField textField;

    public static void main(String[] args) throws Throwable {
        SwingUtilities.invokeAndWait(new Runnable() {

            public void run() {
                createAndShowGUI();
            }
        });
        robot = new Robot();
        Thread.sleep(100);
        robot.setAutoDelay(100);
        runTest1();
    }

    private static void createAndShowGUI() {
        JFrame mainFrame = new JFrame("Bug 8075609 - 1 test");
        JPanel rootPanel = new JPanel();
        rootPanel.setLayout(new BorderLayout());
        JPanel formPanel = new JPanel();
        formPanel.setFocusTraversalPolicy(new LayoutFocusTraversalPolicy());
        formPanel.setFocusCycleRoot(true);
        JRadioButton option1 = new JRadioButton("Option 1", true);
        JRadioButton option2 = new JRadioButton("Option 2");
        ButtonGroup radioButtonGroup = new ButtonGroup();
        radioButtonGroup.add(option1);
        radioButtonGroup.add(option2);
        formPanel.add(option1);
        formPanel.add(option2);
        textField = new JTextField("Another focusable component");
        formPanel.add(textField);
        rootPanel.add(formPanel, BorderLayout.CENTER);
        JButton okButton = new JButton("OK");
        rootPanel.add(okButton, BorderLayout.SOUTH);
        mainFrame.add(rootPanel);
        mainFrame.pack();
        mainFrame.setVisible(true);
        mainFrame.toFront();
    }

    private static void runTest1() throws Exception {
        hitKey(robot, KeyEvent.VK_TAB);
        robot.delay(1000);
        SwingUtilities.invokeAndWait(new Runnable() {

            public void run() {
                if (!textField.hasFocus()) {
                    System.out.println("Radio Button Group Go To Next Component through Tab Key failed");
                    throw new RuntimeException("Focus is not on textField as Expected");
                }
            }
        });
    }

    private static void hitKey(Robot robot, int keycode) {
        robot.keyPress(keycode);
        robot.keyRelease(keycode);
        robot.waitForIdle();
    }
}
