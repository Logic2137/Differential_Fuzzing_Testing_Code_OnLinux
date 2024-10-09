import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class bug4368790 {

    private static JButton b1;

    private static void createGui() {
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());
        b1 = new JButton("Button1");
        frame.add(b1);
        frame.add(new JButton("Button2"));
        frame.setSize(200, 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        b1.requestFocus();
    }

    public static void main(String[] args) throws Exception {
        Robot robot = new Robot();
        robot.setAutoDelay(50);
        SwingUtilities.invokeAndWait(new Runnable() {

            public void run() {
                bug4368790.createGui();
            }
        });
        robot.waitForIdle();
        robot.keyPress(KeyEvent.VK_SPACE);
        robot.keyPress(KeyEvent.VK_TAB);
        robot.keyRelease(KeyEvent.VK_TAB);
        robot.keyRelease(KeyEvent.VK_SPACE);
        robot.waitForIdle();
        if (b1.getModel().isPressed()) {
            throw new RuntimeException("The button is unexpectedly pressed");
        }
    }
}
