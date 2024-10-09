import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class DiacriticsDrawingTest {

    private static final Font FONT = new Font("Menlo", Font.PLAIN, 12);

    private static final int IMAGE_WIDTH = 20;

    private static final int IMAGE_HEIGHT = 20;

    private static final int TEXT_X = 5;

    private static final int TEXT_Y = 15;

    public static void main(String[] args) {
        BufferedImage composed = drawString("\u00e1");
        BufferedImage decomposed = drawString("a\u0301");
        if (!imagesAreEqual(composed, decomposed)) {
            throw new RuntimeException("Text rendering is supposed to be the same");
        }
    }

    private static BufferedImage drawString(String text) {
        BufferedImage image = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.createGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
        g.setColor(Color.black);
        g.setFont(FONT);
        g.drawString(text, TEXT_X, TEXT_Y);
        g.dispose();
        return image;
    }

    private static boolean imagesAreEqual(BufferedImage i1, BufferedImage i2) {
        if (i1.getWidth() != i2.getWidth() || i1.getHeight() != i2.getHeight())
            return false;
        for (int i = 0; i < i1.getWidth(); i++) {
            for (int j = 0; j < i1.getHeight(); j++) {
                if (i1.getRGB(i, j) != i2.getRGB(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }
}
