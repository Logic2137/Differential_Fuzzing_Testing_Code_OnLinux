import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;

public class TestJSliderRendering {

    private static JFrame frame;

    private static JSlider slider;

    private static Point point;

    private static Rectangle rect;

    private static Robot robot;

    private static final String GTK_LAF_CLASS = "GTKLookAndFeel";

    private static int minColorDifference = 50;

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

    private static int getMaxColorDiff(Color c1, Color c2) {
        return Math.max(Math.abs(c1.getRed() - c2.getRed()), Math.max(Math.abs(c1.getGreen() - c2.getGreen()), Math.abs(c1.getBlue() - c2.getBlue())));
    }

    public static void main(String[] args) throws Exception {
        if (!System.getProperty("os.name").startsWith("Linux")) {
            System.out.println("This test is meant for Linux platform only");
            return;
        }
        for (UIManager.LookAndFeelInfo lookAndFeelInfo : UIManager.getInstalledLookAndFeels()) {
            if (lookAndFeelInfo.getClassName().contains(GTK_LAF_CLASS)) {
                try {
                    UIManager.setLookAndFeel(lookAndFeelInfo.getClassName());
                } catch (final UnsupportedLookAndFeelException ignored) {
                    System.out.println("GTK L&F could not be set, so this " + "test can not be run in this scenario ");
                    return;
                }
            }
        }
        robot = new Robot();
        robot.setAutoDelay(100);
        try {
            SwingUtilities.invokeAndWait(new Runnable() {

                public void run() {
                    JPanel panel = new JPanel();
                    slider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
                    panel.add(slider);
                    frame = new JFrame("TestJSliderRendering");
                    frame.add(panel);
                    frame.setSize(200, 200);
                    frame.setAlwaysOnTop(true);
                    frame.setLocationRelativeTo(null);
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setVisible(true);
                }
            });
            robot.waitForIdle();
            robot.delay(500);
            blockTillDisplayed(slider);
            SwingUtilities.invokeAndWait(() -> {
                point = slider.getLocationOnScreen();
                rect = slider.getBounds();
            });
            robot.waitForIdle();
            robot.delay(500);
            int h = point.y + rect.height * 6 / 7;
            Color backgroundColor = robot.getPixelColor(point.x + rect.width / 4, h);
            robot.waitForIdle();
            boolean knobFound = false;
            for (int i = point.x + rect.width / 4; i < point.x + rect.width * 3 / 4; i += 2) {
                Color highlightColor = robot.getPixelColor(i, h);
                if (getMaxColorDiff(backgroundColor, highlightColor) > minColorDifference) {
                    knobFound = true;
                    break;
                }
                robot.waitForIdle();
            }
            if (!knobFound) {
                throw new RuntimeException("The slider is not rendered properly");
            }
        } finally {
            if (frame != null) {
                SwingUtilities.invokeAndWait(frame::dispose);
            }
        }
    }
}
