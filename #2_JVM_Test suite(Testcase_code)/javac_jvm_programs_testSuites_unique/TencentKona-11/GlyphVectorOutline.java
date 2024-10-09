

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.AttributedString;

import javax.imageio.ImageIO;


public class GlyphVectorOutline {
    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            throw new Error("Usage: java GlyphVectorOutline fontfile outputfile");
        }
        writeImage(new File(args[0]),
                   new File(args[1]),
                   "\u30AF");
    }

    public static void writeImage(File fontFile, File outputFile, String value) throws Exception {
        BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, image.getWidth(), image.getHeight());
        g.setColor(Color.BLACK);

        Font font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
        font = font.deriveFont(Font.PLAIN, 72f);
        FontRenderContext frc = new FontRenderContext(null, false, false);
        GlyphVector gv = font.createGlyphVector(frc, value);
        g.drawGlyphVector(gv, 10, 80);
        g.fill(gv.getOutline(10, 180));
        ImageIO.write(image, "png", outputFile);
    }

    private static void drawString(Graphics2D g, Font font, String value, float x, float y) {
        AttributedString str = new AttributedString(value);
        str.addAttribute(TextAttribute.FOREGROUND, Color.BLACK);
        str.addAttribute(TextAttribute.FONT, font);
        FontRenderContext frc = new FontRenderContext(null, true, true);
        TextLayout layout = new LineBreakMeasurer(str.getIterator(), frc).nextLayout(Integer.MAX_VALUE);
        layout.draw(g, x, y);
    }
}
