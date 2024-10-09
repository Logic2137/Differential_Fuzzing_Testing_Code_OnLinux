import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class bug4368790 {

    private static JButton b1;

    private static JFrame frame;

    private static void createGui() {
        frame = new JFrame();
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

    private static void setLookAndFeel(UIManager.LookAndFeelInfo laf) {
        try {
            UIManager.setLookAndFeel(laf.getClassName());
        } catch (UnsupportedLookAndFeelException ignored) {
            System.out.println("Unsupported L&F: " + laf.getClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Exception {
        Robot robot = new Robot();
        for (UIManager.LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels()) {
            System.out.println("Testing L&F: " + laf);
            SwingUtilities.invokeAndWait(() -> setLookAndFeel(laf));
            try {
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
            } finally {
                if (frame != null)
                    SwingUtilities.invokeAndWait(() -> frame.dispose());
            }
            robot.delay(1000);
        }
    }
}
