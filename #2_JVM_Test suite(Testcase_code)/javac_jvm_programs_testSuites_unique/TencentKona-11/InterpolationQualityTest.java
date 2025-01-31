



import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class InterpolationQualityTest {

    private static final int testSize = 4, scaleFactor = 20, tolerance = 3;
    private static final int sw = testSize * scaleFactor;
    private static final int sh = testSize * scaleFactor;

    private Image testImage;
    private VolatileImage vImg;

    public InterpolationQualityTest() {
        testImage = createTestImage();
    }

    private Image createTestImage() {
        BufferedImage bi = new BufferedImage(testSize, testSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = bi.createGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, testSize, testSize);
        for (int i = 0; i < testSize; i++) {
            bi.setRGB(i, i, Color.WHITE.getRGB());
        }
        return bi;
    }

    private BufferedImage createReferenceImage(Object hint) {
        BufferedImage bi = new BufferedImage(sw, sh, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bi.createGraphics();
        drawImage(g2d, hint);
        return bi;
    }

    private void drawImage(Graphics2D g2d, Object hint) {
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
        g2d.drawImage(testImage, 0, 0, sw, sh, null);
    }

    private GraphicsConfiguration getDefaultGC() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().
                getDefaultScreenDevice().getDefaultConfiguration();
    }

    private void createVImg() {
        vImg = getDefaultGC().createCompatibleVolatileImage(sw, sh);
    }

    private void renderOffscreen(Object hint) {
        Graphics2D g = vImg.createGraphics();
        drawImage(g, hint);
        g.dispose();
    }

    private BufferedImage renderImage(Object hint) {
        BufferedImage snapshot;
        createVImg();
        renderOffscreen(hint);

        do {
            int status = vImg.validate(getDefaultGC());
            if (status != VolatileImage.IMAGE_OK) {
                if (status == VolatileImage.IMAGE_INCOMPATIBLE) {
                    createVImg();
                }
                renderOffscreen(hint);
            }
            snapshot = vImg.getSnapshot();
        } while (vImg.contentsLost());
        vImg.flush();
        return snapshot;
    }

    private boolean compareComponent(int comp1, int comp2) {
        return Math.abs(comp1 - comp2) <= tolerance;
    }

    private boolean compareRGB(int rgb1, int rgb2) {
        Color col1 = new Color(rgb1);
        Color col2 = new Color(rgb2);
        return compareComponent(col1.getRed(), col2.getRed()) &&
                compareComponent(col1.getBlue(), col2.getBlue()) &&
                compareComponent(col1.getGreen(), col2.getGreen()) &&
                compareComponent(col1.getAlpha(), col2.getAlpha());
    }

    private boolean compareImages(BufferedImage img, BufferedImage ref, String imgName) {
        for (int y = 0; y < ref.getHeight(); y++) {
            for (int x = 0; x < ref.getWidth(); x++) {
                if (!compareRGB(ref.getRGB(x, y), img.getRGB(x, y))) {
                    System.out.println(imgName + ".getRGB(" + x + ", " + y + ") = "
                            + new Color(img.getRGB(x, y)) + " != "
                            + new Color(ref.getRGB(x, y)));
                    return false;
                }
            }
        }
        return true;
    }

    private boolean test(Object hint) {
        BufferedImage refImage = createReferenceImage(hint);
        BufferedImage resImage = renderImage(hint);

        boolean passed = compareImages(resImage, refImage, "resImage");
        System.out.println(getHintName(hint) + (passed ? " passed." : " failed."));
        if (!passed) {
            dumpImage(refImage, "out_" + getHintName(hint) + "_ref.png");
            dumpImage(resImage, "out_" + getHintName(hint) + ".png");
        }
        return passed;
    }

    public void test() {
        boolean passed = true;
        passed &= test(RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        passed &= test(RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        passed &= test(RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        if (passed) {
            System.out.println("Test PASSED.");
        } else {
            throw new RuntimeException("Test FAILED.");
        }
    }

    private String getHintName(Object hint) {
        if (hint == RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR) {
            return "nearest";
        }
        else if (hint == RenderingHints.VALUE_INTERPOLATION_BILINEAR) {
            return "bilinear";
        }
        else if (hint == RenderingHints.VALUE_INTERPOLATION_BICUBIC) {
            return "bicubic";
        }
        else {
            return "null";
        }
    }

    private void dumpImage(BufferedImage bi, String name) {
        try {
            ImageIO.write(bi, "PNG", new File(name));
        } catch (IOException ex) {
        }
    }

    public static void main(String[] argv) {
        InterpolationQualityTest test = new InterpolationQualityTest();
        test.test();
    }
}
