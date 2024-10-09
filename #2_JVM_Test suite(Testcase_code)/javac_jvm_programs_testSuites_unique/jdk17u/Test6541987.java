import java.awt.AWTException;
import java.awt.Color;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.KeyEvent;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Test6541987 implements Runnable {

    private static Robot robot;

    private static JFrame frame;

    public static void main(String[] args) throws Exception {
        try {
            robot = new Robot();
            robot.setAutoDelay(100);
            start();
            click(KeyEvent.VK_ESCAPE);
            robot.waitForIdle();
            start();
            click(KeyEvent.VK_1);
            click(KeyEvent.VK_0);
            click(KeyEvent.VK_ESCAPE);
            click(KeyEvent.VK_ESCAPE);
            robot.waitForIdle();
            robot.delay(500);
            for (Window window : Window.getWindows()) {
                if (window.isVisible()) {
                    throw new Error("found visible window: " + window.getName());
                }
            }
        } finally {
            if (frame != null) {
                SwingUtilities.invokeAndWait(frame::dispose);
            }
        }
    }

    private static void start() {
        SwingUtilities.invokeLater(new Test6541987());
        click(KeyEvent.VK_ALT, KeyEvent.VK_H);
        click(KeyEvent.VK_TAB);
        click(KeyEvent.VK_TAB);
        click(KeyEvent.VK_TAB);
        click(KeyEvent.VK_TAB);
    }

    private static void click(int... keys) {
        robot.waitForIdle();
        for (int key : keys) {
            robot.keyPress(key);
        }
        for (int key : keys) {
            robot.keyRelease(key);
        }
    }

    public void run() {
        String title = getClass().getName();
        frame = new JFrame(title);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        Color color = JColorChooser.showDialog(frame, title, Color.BLACK);
        if (color != null) {
            throw new Error("unexpected color: " + color);
        }
        frame.setVisible(false);
        frame.dispose();
    }
}
