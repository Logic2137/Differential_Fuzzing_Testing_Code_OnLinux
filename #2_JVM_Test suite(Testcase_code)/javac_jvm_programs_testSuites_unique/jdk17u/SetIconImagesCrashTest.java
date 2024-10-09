import java.awt.Color;
import java.awt.Graphics;
import java.awt.Window;
import java.awt.Frame;
import java.util.List;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.SwingUtilities;

public class SetIconImagesCrashTest {

    public static void main(String[] args) throws Exception {
        List<BufferedImage> imageList = new ArrayList<BufferedImage>();
        imageList.add(new BufferedImage(200, 200, BufferedImage.TYPE_BYTE_BINARY));
        for (int i = 0; i < 10; i++) {
            Frame f = new Frame();
            test(f, imageList);
        }
    }

    public static void test(final Window window, final List<BufferedImage> imageList) throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                for (BufferedImage image : imageList) {
                    Graphics graphics = image.getGraphics();
                    graphics.setColor(Color.RED);
                    graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
                    graphics.dispose();
                }
                window.setIconImages(imageList);
                window.setSize(200, 200);
                window.setVisible(true);
            }
        });
        while (!window.isVisible()) {
            Thread.sleep((long) (20));
        }
        Thread.sleep((long) (50));
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                window.setVisible(false);
                window.dispose();
            }
        });
    }
}
