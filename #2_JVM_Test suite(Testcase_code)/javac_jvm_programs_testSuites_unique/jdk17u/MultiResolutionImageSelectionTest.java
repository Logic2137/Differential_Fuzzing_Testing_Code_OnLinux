import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.AbstractMultiResolutionImage;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.awt.image.MultiResolutionImage;
import java.util.Arrays;
import java.util.List;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;

public class MultiResolutionImageSelectionTest {

    final static BufferedImage GOOD_IMAGE = new BufferedImage(200, 200, TYPE_INT_RGB);

    final static Image BAD_IMAGE = new Image() {

        @Override
        public int getWidth(ImageObserver observer) {
            return -1;
        }

        @Override
        public int getHeight(ImageObserver observer) {
            return 1;
        }

        @Override
        public ImageProducer getSource() {
            return null;
        }

        @Override
        public Graphics getGraphics() {
            return null;
        }

        @Override
        public Object getProperty(String name, ImageObserver observer) {
            return null;
        }
    };

    public static void main(String[] args) {
        Graphics g = GOOD_IMAGE.createGraphics();
        g.setColor(Color.RED);
        g.fillRect(0, 0, GOOD_IMAGE.getWidth(), GOOD_IMAGE.getHeight());
        g.dispose();
        MultiResolutionImage mri = new AbstractMultiResolutionImage() {

            @Override
            protected Image getBaseImage() {
                return GOOD_IMAGE;
            }

            @Override
            public Image getResolutionVariant(double destImageWidth, double destImageHeight) {
                if ((int) destImageHeight == 200) {
                    return GOOD_IMAGE;
                } else {
                    return BAD_IMAGE;
                }
            }

            @Override
            public List<Image> getResolutionVariants() {
                return Arrays.asList(BAD_IMAGE, GOOD_IMAGE, BAD_IMAGE);
            }
        };
        BufferedImage target = new BufferedImage(500, 500, TYPE_INT_RGB);
        Graphics2D g2d = target.createGraphics();
        g2d.drawImage((Image) mri, 0, 0, 500, 500, null);
        g2d.dispose();
        if (Color.RED.getRGB() != target.getRGB(1, 1)) {
            throw new RuntimeException("Wrong resolution variant was used");
        }
    }
}
