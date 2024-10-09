import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;

public class TestJTextPaneBackgroundColor {

    private static JFrame frame;

    private static JTextPane textPane;

    private static Point point;

    private static Rectangle rect;

    private static Robot robot;

    private static final String GTK_LAF_CLASS = "GTKLookAndFeel";

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
                    textPane = new JTextPane();
                    textPane.setText("             ");
                    panel.add(textPane, BorderLayout.CENTER);
                    frame = new JFrame("TestJTextPaneBackgroundColor");
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
            blockTillDisplayed(textPane);
            SwingUtilities.invokeAndWait(() -> {
                point = textPane.getLocationOnScreen();
                rect = textPane.getBounds();
            });
            robot.waitForIdle();
            robot.delay(500);
            Color textpaneBackgroundColor = robot.getPixelColor(point.x + rect.width / 2, point.y + rect.height / 2);
            robot.waitForIdle();
            robot.delay(500);
            Color panelColor = robot.getPixelColor(point.x - rect.width / 2, point.y + rect.height / 2);
            robot.waitForIdle();
            robot.delay(500);
            System.out.println(textpaneBackgroundColor);
            System.out.println(panelColor);
            if (textpaneBackgroundColor.equals(panelColor)) {
                throw new RuntimeException("The expected background color for " + "TextPane was not found");
            }
        } finally {
            if (frame != null) {
                SwingUtilities.invokeAndWait(frame::dispose);
            }
        }
    }
}
