



import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

public class RotatedItalicsTest {
    public static void main(String[] args) throws Exception {
        File fontFile = new File(System.getProperty("test.src", "."), "A.ttf");
        Font baseFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
        Font font = baseFont.deriveFont(Font.ITALIC, 120);

        BufferedImage image = new BufferedImage(100, 100,
                                                BufferedImage.TYPE_INT_RGB);

        Graphics2D g = image.createGraphics();
        g.rotate(Math.PI / 2);
        g.setFont(font);
        g.drawString("A", 10, -10);
        g.dispose();

        if (image.getRGB(50, 76) != Color.white.getRGB()) {
            throw new RuntimeException("Wrong glyph rendering");
        }
    }
}
