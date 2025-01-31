import javax.swing.*;
import java.awt.*;

public class ScrollBarThumbVisibleTest {

    private static JFrame frame;

    private static Point point;

    private static JScrollBar bar;

    public static void main(String[] args) throws Exception {
        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                try {
                    UIManager.setLookAndFeel(info.getClassName());
                } catch (Exception ex) {
                }
                break;
            }
        }
        try {
            SwingUtilities.invokeAndWait(new Runnable() {

                public void run() {
                    frame = new JFrame();
                    frame.setUndecorated(true);
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    setup(frame);
                }
            });
            final Robot robot = new Robot();
            robot.delay(200);
            robot.waitForIdle();
            SwingUtilities.invokeAndWait(new Runnable() {

                @Override
                public void run() {
                    point = bar.getLocationOnScreen();
                }
            });
            Color color1 = robot.getPixelColor(point.x + 48, point.y + 55);
            Color color2 = robot.getPixelColor(point.x + 48, point.y + 125);
            System.out.println(color1);
            System.out.println(color2);
            if (color1.equals(color2)) {
                throw new RuntimeException("Thump is not visible");
            }
        } finally {
            SwingUtilities.invokeAndWait(new Runnable() {

                @Override
                public void run() {
                    if (frame != null) {
                        frame.dispose();
                    }
                }
            });
        }
        System.out.println("ok");
    }

    static void setup(JFrame frame) {
        bar = new JScrollBar(Adjustable.VERTICAL, 500, 0, 0, 1000);
        frame.getContentPane().add(bar);
        frame.setSize(50, 250);
        frame.setLocation(100, 100);
        frame.setVisible(true);
    }
}
