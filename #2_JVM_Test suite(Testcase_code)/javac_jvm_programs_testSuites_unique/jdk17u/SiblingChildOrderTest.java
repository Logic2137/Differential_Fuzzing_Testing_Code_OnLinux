import java.awt.Color;
import java.awt.Robot;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class SiblingChildOrderTest {

    static Color[] colors = new Color[] { Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW };

    static int[] x = new int[] { 200, 150, 100, 50 };

    static int[] y = new int[] { 200, 150, 100, 50 };

    static JDialog[] dlgs = new JDialog[4];

    private static JFrame frame;

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            frame = new JFrame("FRAME");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setUndecorated(true);
            frame.setBounds(50, 50, 400, 400);
            frame.setVisible(true);
        });
        for (int i = 0; i < colors.length; i++) {
            int finalI = i;
            SwingUtilities.invokeLater(() -> {
                dlgs[finalI] = new JDialog(frame, "DLG " + finalI, true);
                dlgs[finalI].getContentPane().setBackground(colors[finalI]);
                dlgs[finalI].setBounds(x[finalI], y[finalI], 200, 200);
                dlgs[finalI].setUndecorated(true);
                dlgs[finalI].setVisible(true);
            });
        }
        Robot robot = new Robot();
        robot.waitForIdle();
        robot.delay(1000);
        for (int i = 0; i < colors.length; i++) {
            Color c = robot.getPixelColor(x[i] + 190, y[i] + 190);
            if (!c.equals(colors[i])) {
                throw new RuntimeException("Expected " + colors[i] + " got " + c);
            }
        }
        for (int i = 0; i < colors.length; i++) {
            SwingUtilities.invokeLater(dlgs[i]::dispose);
        }
        SwingUtilities.invokeLater(frame::dispose);
    }
}
