import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import javax.swing.*;

public class FullWindowContentRenderTest {

    private static final int TD = 10;

    static FullWindowContentRenderTest theTest;

    private Robot robot;

    private JFrame frame;

    private JRootPane rootPane;

    private int DELAY = 1000;

    public FullWindowContentRenderTest() {
        try {
            robot = new Robot();
        } catch (AWTException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void performTest() {
        runSwing(() -> {
            frame = new JFrame("Test");
            frame.setBounds(100, 100, 100, 150);
            rootPane = frame.getRootPane();
            JComponent contentPane = (JComponent) frame.getContentPane();
            JPanel comp = new JPanel() {

                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.setColor(Color.BLUE);
                    g.fillRect(35, 30, 30, 30);
                    g.setColor(Color.GREEN);
                    g.fillRect(35, 60, 30, 30);
                }
            };
            contentPane.add(comp);
            comp.setBackground(Color.RED);
            frame.setVisible(true);
        });
        robot.delay(DELAY);
        final int topInset = frame.getInsets().top;
        final int px = 50, py = topInset + 40;
        Color c = getTestPixel(px, py);
        if (!validateColor(c, Color.BLUE)) {
            throw new RuntimeException("Test failed. Incorrect color " + c + "at (" + px + "," + py + ")");
        }
        runSwing(() -> rootPane.putClientProperty("apple.awt.fullWindowContent", true));
        robot.delay(DELAY);
        c = getTestPixel(50, topInset + 40);
        if (!validateColor(c, Color.GREEN)) {
            throw new RuntimeException("Test failed. Incorrect color " + c + " at (" + px + "," + py + ")");
        }
        runSwing(() -> rootPane.putClientProperty("apple.awt.fullWindowContent", false));
        robot.delay(DELAY);
        c = getTestPixel(50, topInset + 40);
        if (!validateColor(c, Color.BLUE)) {
            throw new RuntimeException("Test failed. Incorrect color " + c + "at (" + px + "," + py + ")");
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
        return Math.abs(c.getRed() - expected.getRed()) <= TD && Math.abs(c.getGreen() - expected.getGreen()) <= TD && Math.abs(c.getBlue() - expected.getBlue()) <= TD;
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
            runSwing(() -> theTest = new FullWindowContentRenderTest());
            theTest.performTest();
        } finally {
            if (theTest != null) {
                runSwing(() -> theTest.dispose());
            }
        }
    }
}
