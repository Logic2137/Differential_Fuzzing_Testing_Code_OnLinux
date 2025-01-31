

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Robot;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


public class ScaledFrameBackgroundTest {

    private static final Color BACKGROUND = Color.RED;
    private static JFrame frame;

    public static void main(String[] args) throws Exception {
        try {
            Robot robot = new Robot();
            robot.setAutoDelay(50);

            SwingUtilities.invokeAndWait(() -> {
                frame = new JFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(400, 300);
                JPanel panel = new JPanel();
                panel.setBackground(BACKGROUND);
                frame.getContentPane().add(panel);
                frame.setVisible(true);
            });

            robot.waitForIdle();
            Thread.sleep(200);

            Rectangle[] rects = new Rectangle[1];
            SwingUtilities.invokeAndWait(() -> {
                rects[0] = frame.getBounds();
            });

            Rectangle bounds = rects[0];

            int x = bounds.x + bounds.width / 4;
            int y = bounds.y + bounds.height / 4;

            Color color = robot.getPixelColor(x, y);

            if (!BACKGROUND.equals(color)) {
                throw new RuntimeException("Wrong backgound color!");
            }

            x = bounds.x + 3 * bounds.width / 4;
            y = bounds.y + 3 * bounds.height / 4;

            color = robot.getPixelColor(x, y);

            if (!BACKGROUND.equals(color)) {
                throw new RuntimeException("Wrong backgound color!");
            }
        } finally {
            if (frame != null) SwingUtilities.invokeAndWait(() -> frame.dispose());
        }
    }
}
