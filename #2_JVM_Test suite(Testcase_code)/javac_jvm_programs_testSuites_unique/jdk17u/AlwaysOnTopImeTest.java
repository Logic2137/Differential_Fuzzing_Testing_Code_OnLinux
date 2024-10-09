import javax.swing.*;
import java.awt.*;

public class AlwaysOnTopImeTest {

    private static JDialog d;

    private static JFrame f;

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(() -> {
            f = new JFrame();
            f.setVisible(true);
            d = new JDialog(f);
            d.add(new JTextField());
            d.pack();
            d.setModal(true);
            f.setAlwaysOnTop(true);
            d.setVisible(true);
            f.setAlwaysOnTop(false);
        });
        Robot robot = new Robot();
        robot.waitForIdle();
        robot.delay(200);
        SwingUtilities.invokeAndWait(() -> d.setVisible(false));
        robot.waitForIdle();
        robot.delay(200);
        SwingUtilities.invokeLater(f::dispose);
    }
}
