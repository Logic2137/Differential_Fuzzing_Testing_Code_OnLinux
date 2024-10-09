import java.awt.Color;
import java.awt.Dialog;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.SwingUtilities;

public class FileDialogIconTest {

    private static Frame frame;

    private static Dialog dialog;

    public static void main(final String[] args) throws Exception {
        Robot robot;
        Point p;
        try {
            frame = new Frame();
            frame.setIconImage(createImage());
            frame.setVisible(true);
            robot = new Robot();
            robot.waitForIdle();
            robot.delay(200);
            dialog = new FileDialog(frame, "Dialog");
            dialog.setModal(false);
            dialog.setVisible(true);
            robot.waitForIdle();
            robot.delay(1000);
            p = new Point(20, 20);
            SwingUtilities.convertPointToScreen(p, dialog);
            Color color = robot.getPixelColor(p.x, p.y);
            if (!Color.RED.equals(color)) {
                throw new RuntimeException("Dialog icon was not inherited from " + "owning window. Wrong color: " + color);
            }
        } finally {
            if (dialog != null) {
                dialog.dispose();
            }
            if (frame != null) {
                frame.dispose();
            }
        }
    }

    private static Image createImage() {
        BufferedImage image = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.getGraphics();
        g.setColor(Color.RED);
        g.fillRect(0, 0, image.getWidth(), image.getHeight());
        g.dispose();
        return image;
    }
}
