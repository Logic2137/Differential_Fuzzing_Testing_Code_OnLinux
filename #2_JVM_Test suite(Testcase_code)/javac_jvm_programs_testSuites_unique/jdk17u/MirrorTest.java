import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class MirrorTest {

    static String target = "\u3042";

    static final int SIZE = 50;

    static final int SHIFT = 20;

    static final int LIMIT = 5;

    static Point getCenterOfGravity(BufferedImage img) {
        int count = 0;
        int sx = 0;
        int sy = 0;
        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                int c = img.getRGB(x, y) & 0xFFFFFF;
                if (c == 0) {
                    count++;
                    sx += x;
                    sy += y;
                }
            }
        }
        if (count == 0) {
            return null;
        } else {
            return new Point(sx / count, sy / count);
        }
    }

    static BufferedImage drawNormal(Font font) {
        BufferedImage image = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, image.getWidth(), image.getHeight());
        g2d.setColor(Color.black);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(target, SHIFT, SHIFT + fm.getAscent());
        g2d.dispose();
        return image;
    }

    static BufferedImage drawVerticalMirror(Font font) {
        BufferedImage image = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, image.getWidth(), image.getHeight());
        g2d.setColor(Color.black);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        AffineTransform base = g2d.getTransform();
        AffineTransform trans = new AffineTransform(1.0, 0, 0, -1.0, 0, 0);
        trans.concatenate(base);
        g2d.setTransform(trans);
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(target, SHIFT, SHIFT - image.getHeight() + fm.getAscent());
        g2d.dispose();
        return image;
    }

    static BufferedImage drawHorizontalMirror(Font font) {
        BufferedImage image = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, image.getWidth(), image.getHeight());
        g2d.setColor(Color.black);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        AffineTransform base = g2d.getTransform();
        AffineTransform trans = new AffineTransform(-1.0, 0, 0, 1.0, 0, 0);
        trans.concatenate(base);
        g2d.setTransform(trans);
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(target, SHIFT - image.getWidth(), SHIFT + fm.getAscent());
        g2d.dispose();
        return image;
    }

    public static void main(String[] args) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Font[] fonts = ge.getAllFonts();
        for (Font font : fonts) {
            if (!font.canDisplay(target.charAt(0))) {
                continue;
            }
            font = font.deriveFont(12.0f);
            BufferedImage img1 = drawNormal(font);
            BufferedImage img2 = drawVerticalMirror(font);
            BufferedImage img3 = drawHorizontalMirror(font);
            Point p1 = getCenterOfGravity(img1);
            Point p2 = getCenterOfGravity(img2);
            Point p3 = getCenterOfGravity(img3);
            if (p1 == null || p2 == null || p3 == null) {
                continue;
            }
            p2.y = SIZE - p2.y - 2;
            p3.x = SIZE - p3.x - 2;
            if (Math.abs(p1.x - p2.x) > LIMIT || Math.abs(p1.y - p2.y) > LIMIT || Math.abs(p1.x - p3.x) > LIMIT || Math.abs(p1.y - p3.y) > LIMIT) {
                System.out.println("Error: " + p1 + "," + p2 + "," + p3);
                throw new RuntimeException("Incorrect mirrored character with " + font);
            }
        }
    }
}
