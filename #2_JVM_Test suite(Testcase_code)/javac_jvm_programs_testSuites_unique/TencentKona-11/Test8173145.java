



import java.awt.AWTException;
import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Test8173145 {

    private volatile static JButton btn;
    private volatile static JFrame f;
    private volatile static boolean uiCreated;

    public static void main(String[] args) throws InvocationTargetException, InterruptedException, AWTException {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    try {
                        uiCreated = createGUI();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            if (uiCreated) {
                test();
            } else {
                
            }
        }finally {
            SwingUtilities.invokeAndWait(() -> f.dispose());
        }
    }

    private static void test() {
        final Robot robot;
        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
        robot.setAutoDelay(150);
        robot.waitForIdle();

        robot.keyPress(KeyEvent.VK_ALT);
        robot.keyPress(KeyEvent.VK_M);
        robot.keyRelease(KeyEvent.VK_M);
        robot.keyRelease(KeyEvent.VK_ALT);
        robot.waitForIdle();

        Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();

        if (focusOwner != btn) {
            throw new RuntimeException("Wrong focus owner");
        }
    }

    private static boolean createGUI() {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
            return false;
        }
        f = new JFrame();

        JPanel panel = new JPanel();
        btn = new JButton("Mmmmm");
        btn.setMnemonic(KeyEvent.VK_M);
        btn.setDisplayedMnemonicIndex(0);
        panel.add(btn);

        JTextField tf = new JTextField();
        tf.setColumns(10);
        panel.add(tf);

        f.setJMenuBar(getMenuBar());
        f.add(panel);
        f.pack();
        f.setVisible(true);
        f.setLocationRelativeTo(null);
        tf.requestFocus();
        return true;
    }

    static JMenuBar getMenuBar() {
        JMenuBar menuBar;
        JMenu menu;

        menuBar = new JMenuBar();

        menu = new JMenu("Menu");
        menuBar.add(menu);

        JMenuItem mi = new JMenuItem("test");
        menu.add(mi);

        return menuBar;
    }
}
