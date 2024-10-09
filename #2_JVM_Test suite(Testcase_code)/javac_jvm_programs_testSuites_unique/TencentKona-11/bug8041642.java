



import javax.swing.*;
import java.awt.*;

public class bug8041642 {

    private static JFrame frame;
    private static Point point;
    private static JProgressBar bar;

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
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
            robot.delay(300);
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    point = bar.getLocationOnScreen();
                }
            });
            Color color = robot.getPixelColor(point.x + 1, point.y + 7);
            System.out.println(color);
            if (color.getGreen() < 150 || color.getBlue() > 30 ||
                    color.getRed() > 200) {
                throw new RuntimeException("Bar padding color should be green");
            }

        } finally {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    frame.dispose();
                }
            });
        }

        System.out.println("ok");
    }

    static void setup(JFrame frame) {
        bar = new JProgressBar();
        bar.setBackground(Color.WHITE);
        bar.setValue(2);
        frame.getContentPane().add(bar, BorderLayout.NORTH);
        frame.getContentPane().setBackground(Color.GREEN);
        frame.setSize(200, 150);
        frame.setLocation(100, 100);
        frame.setVisible(true);
    }
}
