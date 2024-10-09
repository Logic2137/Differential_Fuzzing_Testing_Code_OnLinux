import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class NonInvertibleTransformTextTest {

    public static void main(String[] args) {
        AffineTransform at = new AffineTransform(1f, 0.0f, -15, 0.0, -1, -30);
        FontRenderContext frc = new FontRenderContext(at, false, false);
        Font font = new Font(Font.DIALOG, Font.PLAIN, 12);
        TextLayout tl = new TextLayout("ABC", font, frc);
        tl.getOutline(new AffineTransform());
        BufferedImage bi = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bi.createGraphics();
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, 100, 100);
        g2d.setColor(Color.red);
        tl.draw(g2d, 50, 50);
        Font f = g2d.getFont();
        char[] chs = { 'A', 'B', 'C' };
        GlyphVector gv = f.layoutGlyphVector(frc, chs, 0, chs.length, 0);
        g2d.drawGlyphVector(gv, 20, 20);
        g2d.setTransform(at);
        g2d.drawString("ABC", 20, 20);
        g2d.drawChars(chs, 0, chs.length, 20, 20);
        tl.draw(g2d, 50, 50);
    }
}
