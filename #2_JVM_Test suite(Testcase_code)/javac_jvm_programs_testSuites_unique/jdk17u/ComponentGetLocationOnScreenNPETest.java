import javax.swing.*;
import java.awt.*;

public class ComponentGetLocationOnScreenNPETest {

    private static Frame frame;

    private static JPanel panel;

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            frame = new Frame();
            JPanel parentPanel = new JPanel() {

                @Override
                public Container getParent() {
                    return new Frame();
                }
            };
            frame.add(parentPanel);
            panel = new JPanel();
            parentPanel.add(panel);
            frame.setVisible(true);
        });
        Robot robot = new Robot();
        robot.waitForIdle();
        robot.delay(200);
        SwingUtilities.invokeAndWait(panel::getLocationOnScreen);
        SwingUtilities.invokeLater(frame::dispose);
    }
}
