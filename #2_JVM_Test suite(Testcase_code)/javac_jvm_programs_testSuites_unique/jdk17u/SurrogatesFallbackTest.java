import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;

public class SurrogatesFallbackTest {

    private static final int CHARACTER = 0x1d400;

    private static final Font FONT = new Font("Menlo", Font.PLAIN, 12);

    private static final int IMAGE_WIDTH = 20;

    private static final int IMAGE_HEIGHT = 20;

    private static final int GLYPH_X = 5;

    private static final int GLYPH_Y = 15;

    public static void main(String[] args) {
        BufferedImage noGlyph = createImage(g -> {
        });
        BufferedImage missingGlyph = createImage(g -> {
            GlyphVector gv = FONT.createGlyphVector(g.getFontRenderContext(), new int[] { FONT.getMissingGlyphCode() });
            g.drawGlyphVector(gv, GLYPH_X, GLYPH_Y);
        });
        BufferedImage surrogateCharGlyph = createImage(g -> {
            g.setFont(FONT);
            g.drawString(new String(Character.toChars(CHARACTER)), GLYPH_X, GLYPH_Y);
        });
        if (imagesAreEqual(surrogateCharGlyph, noGlyph)) {
            throw new RuntimeException("Character was not rendered");
        }
        if (imagesAreEqual(surrogateCharGlyph, missingGlyph)) {
            throw new RuntimeException("Character is rendered as missing");
        }
    }

    private static BufferedImage createImage(Consumer<Graphics2D> drawing) {
        BufferedImage image = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
        g.setColor(Color.black);
        drawing.accept(g);
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
