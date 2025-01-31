import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;
import javax.swing.ButtonGroup;
import javax.swing.FocusManager;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class FocusTraversal {

    private static JFrame frame;

    private static JRadioButton a;

    private static JRadioButton b;

    private static JRadioButton c;

    private static JRadioButton d;

    private static JTextField next;

    private static JTextField prev;

    private static Robot robot;

    public static void main(String[] args) throws Exception {
        robot = new Robot();
        robot.setAutoDelay(100);
        robot.waitForIdle();
        UIManager.LookAndFeelInfo[] lookAndFeelArray = UIManager.getInstalledLookAndFeels();
        for (UIManager.LookAndFeelInfo lookAndFeelItem : lookAndFeelArray) {
            executeCase(lookAndFeelItem.getClassName());
        }
    }

    private static void executeCase(String lookAndFeelString) throws Exception {
        if (tryLookAndFeel(lookAndFeelString)) {
            createUI(lookAndFeelString);
            robot.waitForIdle();
            runTestCase();
            robot.waitForIdle();
            cleanUp();
            robot.waitForIdle();
        }
    }

    private static void createUI(final String lookAndFeelString) throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                Set<KeyStroke> keystrokes = new HashSet<KeyStroke>();
                keystrokes.add(KeyStroke.getKeyStroke("TAB"));
                keystrokes.add(KeyStroke.getKeyStroke("ENTER"));
                frame = new JFrame("FocusTraversalTest " + lookAndFeelString);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setUndecorated(true);
                frame.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, keystrokes);
                a = new JRadioButton("a");
                b = new JRadioButton("b");
                c = new JRadioButton("c");
                d = new JRadioButton("d");
                ButtonGroup radioButtonGroup = new ButtonGroup();
                radioButtonGroup.add(a);
                radioButtonGroup.add(b);
                radioButtonGroup.add(c);
                radioButtonGroup.add(d);
                JPanel panel = new JPanel();
                prev = new JTextField("text");
                panel.add(prev);
                panel.add(a);
                panel.add(b);
                panel.add(c);
                panel.add(d);
                next = new JTextField("text");
                panel.add(next);
                JPanel root = new JPanel();
                root.setLayout(new BorderLayout());
                root.add(panel, BorderLayout.CENTER);
                root.add(new JButton("OK"), BorderLayout.SOUTH);
                frame.add(root);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                frame.toFront();
            }
        });
    }

    private static void runTestCase() throws Exception {
        focusOn(a);
        robot.waitForIdle();
        robot.delay(500);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        robot.waitForIdle();
        isFocusOwner(next, "forward");
        robot.keyPress(KeyEvent.VK_SHIFT);
        robot.keyPress(KeyEvent.VK_TAB);
        robot.keyRelease(KeyEvent.VK_TAB);
        robot.keyRelease(KeyEvent.VK_SHIFT);
        robot.waitForIdle();
        isFocusOwner(a, "backward");
    }

    private static void focusOn(Component component) throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                component.requestFocusInWindow();
            }
        });
    }

    private static void isFocusOwner(Component queriedFocusOwner, String direction) throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                Component actualFocusOwner = FocusManager.getCurrentManager().getFocusOwner();
                if (actualFocusOwner != queriedFocusOwner) {
                    frame.dispose();
                    throw new RuntimeException("Focus component is wrong after " + direction + " direction ");
                }
            }
        });
    }

    private static boolean tryLookAndFeel(String lookAndFeelString) throws Exception {
        try {
            UIManager.setLookAndFeel(lookAndFeelString);
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            return false;
        }
        System.out.println("Testing lookAndFeel " + lookAndFeelString);
        return true;
    }

    private static void cleanUp() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                frame.dispose();
            }
        });
    }
}
