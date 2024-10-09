import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Panel;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import javax.swing.UIManager;

public class HiDPIRobotScreenCaptureTest {

    private static final Color[] COLORS = { Color.GREEN, Color.BLUE, Color.ORANGE, Color.RED };

    public static void main(String[] args) throws Exception {
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            try {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            } catch (Exception e) {
                return;
            }
        }
        Frame frame = new Frame();
        frame.setBounds(40, 30, 400, 300);
        frame.setUndecorated(true);
        Panel panel = new Panel(new BorderLayout());
        Canvas canvas = new Canvas() {

            @Override
            public void paint(Graphics g) {
                super.paint(g);
                int w = getWidth();
                int h = getHeight();
                g.setColor(COLORS[0]);
                g.fillRect(0, 0, w / 2, h / 2);
                g.setColor(COLORS[1]);
                g.fillRect(w / 2, 0, w / 2, h / 2);
                g.setColor(COLORS[2]);
                g.fillRect(0, h / 2, w / 2, h / 2);
                g.setColor(COLORS[3]);
                g.fillRect(w / 2, h / 2, w / 2, h / 2);
            }
        };
        panel.add(canvas);
        frame.add(panel);
        frame.setVisible(true);
        Robot robot = new Robot();
        robot.waitForIdle();
        Thread.sleep(200);
        Rectangle rect = canvas.getBounds();
        rect.setLocation(canvas.getLocationOnScreen());
        BufferedImage image = robot.createScreenCapture(rect);
        frame.dispose();
        int w = image.getWidth();
        int h = image.getHeight();
        if (w != frame.getWidth() || h != frame.getHeight()) {
            throw new RuntimeException("Wrong image size!");
        }
        if (image.getRGB(w / 4, h / 4) != COLORS[0].getRGB()) {
            throw new RuntimeException("Wrong image color!");
        }
        if (image.getRGB(3 * w / 4, h / 4) != COLORS[1].getRGB()) {
            throw new RuntimeException("Wrong image color!");
        }
        if (image.getRGB(w / 4, 3 * h / 4) != COLORS[2].getRGB()) {
            throw new RuntimeException("Wrong image color!");
        }
        if (image.getRGB(3 * w / 4, 3 * h / 4) != COLORS[3].getRGB()) {
            throw new RuntimeException("Wrong image color!");
        }
    }
}
