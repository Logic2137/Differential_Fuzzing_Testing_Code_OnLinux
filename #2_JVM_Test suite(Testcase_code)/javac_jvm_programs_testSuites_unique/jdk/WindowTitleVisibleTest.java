



import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import javax.swing.*;

public class WindowTitleVisibleTest
{
    private static final int TD = 10;
    static WindowTitleVisibleTest theTest;
    private Robot robot;
    private JFrame frame;
    private JRootPane rootPane;

    private int DELAY = 1000;

    public WindowTitleVisibleTest() {
        try {
            robot = new Robot();
        } catch (AWTException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void performTest() {

        runSwing(() -> {
            frame = new JFrame("IIIIIIIIIIIIIIII");
            frame.setBounds(100, 100, 300, 150);
            rootPane = frame.getRootPane();
            JComponent contentPane = (JComponent) frame.getContentPane();
            JPanel comp = new JPanel();
            contentPane.add(comp);
            comp.setBackground(Color.RED);
            frame.setVisible(true);
        });

        robot.delay(DELAY);
        runSwing(() -> rootPane.putClientProperty("apple.awt.fullWindowContent", true));
        runSwing(() -> rootPane.putClientProperty("apple.awt.transparentTitleBar", true));
        runSwing(() -> rootPane.putClientProperty("apple.awt.windowTitleVisible", false));
        robot.delay(DELAY);

        for (int px = 140; px < 160; px++) {
            for (int py = 5; py < 20; py++) {
                Color c = getTestPixel(px, py);
                if (!validateColor(c, Color.RED)) {
                    throw new RuntimeException("Test failed. Incorrect color " + c +
                            "at (" + px + "," + py + ")");
                }
            }
        }

        runSwing(() -> frame.dispose());

        frame = null;
        rootPane = null;
    }

    private Color getTestPixel(int x, int y) {
        Rectangle bounds = frame.getBounds();
        BufferedImage screenImage = robot.createScreenCapture(bounds);
        int rgb = screenImage.getRGB(x, y);
        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = rgb & 0xFF;
        Color c = new Color(red, green, blue);
        return c;
    }

    private boolean validateColor(Color c, Color expected) {
        return Math.abs(c.getRed() - expected.getRed()) <= TD &&
            Math.abs(c.getGreen() - expected.getGreen()) <= TD &&
            Math.abs(c.getBlue() - expected.getBlue()) <= TD;
    }

    public void dispose() {
        if (frame != null) {
            frame.dispose();
            frame = null;
        }
    }

    private static void runSwing(Runnable r) {
        try {
            SwingUtilities.invokeAndWait(r);
        } catch (InterruptedException e) {
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        if (!System.getProperty("os.name").contains("OS X")) {
            System.out.println("This test is for MacOS only. Automatically passed on other platforms.");
            return;
        }

        try {
            runSwing(() -> theTest = new WindowTitleVisibleTest());
            theTest.performTest();
        } finally {
            if (theTest != null) {
                runSwing(() -> theTest.dispose());
            }
        }
    }
}
