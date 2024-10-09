import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class AntialiasedTextArtifact {

    private static final int TEST_IMAGE_WIDTH = 2800;

    private static final int TEST_IMAGE_HEIGHT = 100;

    private static final String TEST_STRING = "The quick brown fox jumps over the lazy dog. 0123456789.";

    private static final int[] TYPES = { BufferedImage.TYPE_INT_ARGB, BufferedImage.TYPE_INT_ARGB_PRE, BufferedImage.TYPE_4BYTE_ABGR, BufferedImage.TYPE_4BYTE_ABGR_PRE, BufferedImage.TYPE_INT_RGB, BufferedImage.TYPE_INT_BGR, BufferedImage.TYPE_3BYTE_BGR };

    public static void main(String[] args) throws IOException {
        for (int type : TYPES) {
            BufferedImage testImg = getBufferedImage(type);
            drawAntialiasedString(testImg);
            checkArtifact(testImg);
        }
    }

    private static BufferedImage getBufferedImage(int imageType) {
        BufferedImage image = new BufferedImage(TEST_IMAGE_WIDTH, TEST_IMAGE_HEIGHT, imageType);
        return image;
    }

    private static void drawAntialiasedString(BufferedImage image) {
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setColor(new Color(127, 127, 127, 127));
        graphics.fillRect(0, 0, TEST_IMAGE_WIDTH, TEST_IMAGE_HEIGHT);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        Font font = new Font("Verdana", Font.PLAIN, 60);
        graphics.setFont(font);
        graphics.setColor(new Color(255, 0, 0));
        graphics.drawString(TEST_STRING, 10, 75);
        graphics.dispose();
    }

    private static void checkArtifact(BufferedImage image) throws IOException {
        int componentMask = 0xff;
        int colorThreshold = 200;
        int rowIndex = 0;
        int colIndex = 0;
        for (rowIndex = 0; rowIndex < image.getHeight(); rowIndex++) {
            for (colIndex = 0; colIndex < image.getWidth(); colIndex++) {
                int colorValue = image.getRGB(colIndex, rowIndex);
                int colorComponent1 = colorValue & componentMask;
                int colorComponent2 = (colorValue >> 8) & componentMask;
                int colorComponent3 = (colorValue >> 16) & componentMask;
                if (colorComponent1 >= colorThreshold && colorComponent2 >= colorThreshold && colorComponent3 >= colorThreshold) {
                    throw new RuntimeException("Test Failed.");
                }
            }
        }
    }
}
