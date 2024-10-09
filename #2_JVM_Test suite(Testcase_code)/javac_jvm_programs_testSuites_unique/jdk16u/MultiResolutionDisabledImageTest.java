

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.BaseMultiResolutionImage;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.GrayFilter;
import java.awt.image.MultiResolutionImage;
import javax.swing.JLabel;


public class MultiResolutionDisabledImageTest {

    private static final String IMAGE_NAME_1X = "image.png";
    private static final String IMAGE_NAME_2X = "image@2x.png";
    private static final int IMAGE_SIZE = 100;
    private static final Color COLOR_1X = Color.GREEN;
    private static final Color COLOR_2X = Color.BLUE;

    public static void main(String[] args) throws Exception {

        Image baseMRImage = new BaseMultiResolutionImage(createImage(1),
                                                         createImage(2));
        testMRDisabledImage(baseMRImage);

        saveImages();
        Image toolkitMRImage = Toolkit.getDefaultToolkit().getImage(IMAGE_NAME_1X);

        if (toolkitMRImage instanceof MultiResolutionImage) {
            testMRDisabledImage(toolkitMRImage);
        }
    }

    private static void testMRDisabledImage(Image image) throws Exception {

        Image disabledImage = GrayFilter.createDisabledImage(image);
        MediaTracker mediaTracker = new MediaTracker(new JLabel());
        mediaTracker.addImage(disabledImage, 0);
        mediaTracker.waitForID(0);

        BufferedImage buffImage = new BufferedImage(IMAGE_SIZE,
                                                    IMAGE_SIZE,
                                                    BufferedImage.TYPE_INT_RGB);

        int x = IMAGE_SIZE / 2;
        int y = IMAGE_SIZE / 2;

        Graphics2D g = buffImage.createGraphics();

        g.scale(1, 1);
        g.drawImage(disabledImage, 0, 0, null);
        int rgb1x = buffImage.getRGB(x, y);

        g.scale(2, 2);
        g.drawImage(disabledImage, 0, 0, null);
        int rgb2x = buffImage.getRGB(x, y);

        g.dispose();

        if (rgb1x == rgb2x) {
            throw new RuntimeException("Disabled image is the same for the base"
                    + "image and the resolution variant");
        }

    }

    private static BufferedImage createImage(int scale) throws Exception {
        BufferedImage image = new BufferedImage(scale * 200, scale * 300,
                                                BufferedImage.TYPE_INT_RGB);
        Graphics g = image.createGraphics();
        g.setColor(scale == 1 ? COLOR_1X : COLOR_2X);
        g.fillRect(0, 0, scale * 200, scale * 300);
        g.dispose();
        return image;
    }

    private static void saveImages() throws Exception {
        saveImage(createImage(1), IMAGE_NAME_1X);
        saveImage(createImage(2), IMAGE_NAME_2X);
    }

    private static void saveImage(BufferedImage image, String name) throws Exception {
        ImageIO.write(image, "png", new File(name));
    }
}
