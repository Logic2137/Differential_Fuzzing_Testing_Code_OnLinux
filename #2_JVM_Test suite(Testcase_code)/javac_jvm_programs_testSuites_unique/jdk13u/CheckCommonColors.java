import java.awt.Color;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Robot;
import java.util.List;

public final class CheckCommonColors {

    private static final Frame frame = new Frame();

    private static Robot robot;

    public static void main(final String[] args) throws Exception {
        robot = new Robot();
        try {
            test();
        } finally {
            frame.dispose();
        }
    }

    private static void test() {
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);
        frame.setUndecorated(true);
        for (final Color color : List.of(Color.WHITE, Color.LIGHT_GRAY, Color.GRAY, Color.DARK_GRAY, Color.BLACK, Color.RED, Color.PINK, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.MAGENTA, Color.CYAN, Color.BLUE)) {
            frame.dispose();
            frame.setBackground(color);
            frame.setVisible(true);
            checkPixels(color);
        }
    }

    private static void checkPixels(final Color color) {
        int attempt = 0;
        while (true) {
            Point p = frame.getLocationOnScreen();
            Color pixel = robot.getPixelColor(p.x + frame.getWidth() / 2, p.y + frame.getHeight() / 2);
            if (color.equals(pixel)) {
                return;
            }
            if (attempt > 10) {
                System.err.println("Expected: " + color);
                System.err.println("Actual: " + pixel);
                throw new RuntimeException("Too many attempts: " + attempt);
            }
            robot.delay((int) Math.pow(2.2, attempt++));
        }
    }
}
