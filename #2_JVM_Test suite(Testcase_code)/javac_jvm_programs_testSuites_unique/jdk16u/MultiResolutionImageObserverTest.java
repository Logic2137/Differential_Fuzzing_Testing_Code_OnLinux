
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import static java.awt.image.ImageObserver.*;
import java.io.File;
import javax.imageio.ImageIO;


public class MultiResolutionImageObserverTest {

    private static final int TIMEOUT = 2000;

    public static void main(String[] args) throws Exception {

        generateImages();
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image image = Toolkit.getDefaultToolkit().getImage(IMAGE_NAME_1X);

        LoadImageObserver sizeObserver
                = new LoadImageObserver(WIDTH | HEIGHT);
        toolkit.prepareImage(image, -1, -1, sizeObserver);
        waitForImageLoading(sizeObserver, "The first observer is not called");

        LoadImageObserver bitsObserver
                = new LoadImageObserver(SOMEBITS | FRAMEBITS | ALLBITS);

        BufferedImage buffImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = (Graphics2D) buffImage.createGraphics();
        g2d.scale(2, 2);
        g2d.drawImage(image, 0, 0, bitsObserver);
        waitForImageLoading(bitsObserver, "The second observer is not called!");
        g2d.dispose();
    }

    private static void waitForImageLoading(LoadImageObserver observer,
            String errorMessage) throws Exception {

        long endTime = System.currentTimeMillis() + TIMEOUT;

        while (!observer.loaded && System.currentTimeMillis() < endTime) {
            Thread.sleep(TIMEOUT / 100);
        }

        if (!observer.loaded) {
            throw new RuntimeException(errorMessage);
        }
    }

    private static final String IMAGE_NAME_1X = "image.png";
    private static final String IMAGE_NAME_2X = "image@2x.png";

    private static void generateImages() throws Exception {
        generateImage(1);
        generateImage(2);
    }

    private static void generateImage(int scale) throws Exception {
        BufferedImage image = new BufferedImage(
                scale * 200, scale * 300,
                BufferedImage.TYPE_INT_RGB);
        Graphics g = image.createGraphics();
        g.setColor(scale == 1 ? Color.GREEN : Color.BLUE);
        g.fillRect(0, 0, scale * 200, scale * 300);
        File file = new File(scale == 1 ? IMAGE_NAME_1X : IMAGE_NAME_2X);
        ImageIO.write(image, "png", file);
        g.dispose();
    }

    private static class LoadImageObserver implements ImageObserver {

        private final int infoflags;
        private volatile boolean loaded;

        public LoadImageObserver(int flags) {
            this.infoflags = flags;
        }

        @Override
        public boolean imageUpdate(Image img, int flags, int x, int y, int width, int height) {

            if ((flags & infoflags) != 0) {
                loaded = true;
            }

            return !loaded;
        }
    }
}
