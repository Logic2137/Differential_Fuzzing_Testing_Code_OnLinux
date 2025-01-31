



import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.KeyEvent;

public class TestButtonGroupFocusTraversal {
    private static JFrame frame;
    private static JTextField textFieldFirst, textFieldLast;
    private static JToggleButton toggleButton1, toggleButton2;
    private static JRadioButton radioButton1, radioButton2;
    private static Robot robot;

    private static void blockTillDisplayed(Component comp) {
        Point p = null;
        while (p == null) {
            try {
                p = comp.getLocationOnScreen();
            } catch (IllegalStateException e) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ie) {
                }
            }
        }
    }

    private static void createUI() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                textFieldFirst = new JTextField("First");
                textFieldLast = new JTextField("Last");
                toggleButton1 = new JToggleButton("1");
                toggleButton2 = new JToggleButton("2");
                radioButton1 = new JRadioButton("1");
                radioButton2 = new JRadioButton("2");

                ButtonGroup toggleGroup = new ButtonGroup();
                toggleGroup.add(toggleButton1);
                toggleGroup.add(toggleButton2);

                ButtonGroup radioGroup = new ButtonGroup();
                radioGroup.add(radioButton1);
                radioGroup.add(radioButton2);

                toggleButton2.setSelected(true);
                radioButton2.setSelected(true);

                frame = new JFrame("Test");
                frame.setLayout(new FlowLayout());

                Container pane = frame.getContentPane();
                pane.add(textFieldFirst);
                pane.add(toggleButton1);
                pane.add(toggleButton2);
                pane.add(radioButton1);
                pane.add(radioButton2);
                pane.add(textFieldLast);

                frame.pack();
                frame.setAlwaysOnTop(true);
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }

    private static void pressKey(int ...keys) {
        int num = keys.length;
        for (int i=0; i<num; i++)
            robot.keyPress(keys[i]);
        for (int i=num; i>0; i--)
            robot.keyRelease(keys[i-1]);

        robot.waitForIdle();
        robot.delay(200);
    }

    private static void checkFocusedComponent (Component component) {
        Component focusedComponent = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
        if (!focusedComponent.equals(component)) {
            System.out.println(component);
            System.out.println(focusedComponent);
            throw new RuntimeException("Wrong Component Selected");
        }
    }

    public static void main(String[] args) throws Exception {
        robot = new Robot();
        robot.setAutoDelay(100);

        UIManager.LookAndFeelInfo infos[] = UIManager.getInstalledLookAndFeels();
        for (UIManager.LookAndFeelInfo info : infos) {
            UIManager.setLookAndFeel(info.getClassName());
            System.out.println(info.getClassName());
            try {
                createUI();

                robot.waitForIdle();
                robot.delay(200);

                blockTillDisplayed(frame);

                SwingUtilities.invokeAndWait(textFieldFirst::requestFocus);

                if (!textFieldFirst.equals(KeyboardFocusManager.getCurrentKeyboardFocusManager()
                        .getFocusOwner())) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    SwingUtilities.invokeAndWait(textFieldFirst::requestFocus);
                }

                robot.waitForIdle();
                robot.delay(200);

                pressKey(KeyEvent.VK_TAB);
                checkFocusedComponent(toggleButton2);

                pressKey(KeyEvent.VK_TAB);
                checkFocusedComponent(radioButton2);

                pressKey(KeyEvent.VK_TAB);
                checkFocusedComponent(textFieldLast);

                pressKey(KeyEvent.VK_SHIFT, KeyEvent.VK_TAB);
                checkFocusedComponent(radioButton2);

                pressKey(KeyEvent.VK_SHIFT, KeyEvent.VK_TAB);
                checkFocusedComponent(toggleButton2);

                pressKey(KeyEvent.VK_SHIFT, KeyEvent.VK_TAB);
                checkFocusedComponent(textFieldFirst);

                pressKey(KeyEvent.VK_TAB);
                checkFocusedComponent(toggleButton2);

                pressKey(KeyEvent.VK_LEFT);
                checkFocusedComponent(toggleButton1);

                pressKey(KeyEvent.VK_RIGHT);
                checkFocusedComponent(toggleButton2);

                pressKey(KeyEvent.VK_UP);
                checkFocusedComponent(toggleButton1);

                pressKey(KeyEvent.VK_DOWN);
                checkFocusedComponent(toggleButton2);

                pressKey(KeyEvent.VK_TAB);
                checkFocusedComponent(radioButton2);

                pressKey(KeyEvent.VK_LEFT);
                checkFocusedComponent(radioButton1);

                pressKey(KeyEvent.VK_RIGHT);
                checkFocusedComponent(radioButton2);

                pressKey(KeyEvent.VK_UP);
                checkFocusedComponent(radioButton1);

                pressKey(KeyEvent.VK_DOWN);
                checkFocusedComponent(radioButton2);

            } finally {
                if (frame != null) {
                    SwingUtilities.invokeAndWait(frame::dispose);
                }
            }
        }
    }
}

