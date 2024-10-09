import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class GlyphRotationTest {

    public static final String fontName = "MS UI Gothic";

    public static Font font;

    public static final int SZ = 50;

    public static void main(String[] args) {
        font = new Font(fontName, Font.PLAIN, 15);
        if (!font.getFamily(java.util.Locale.ENGLISH).equals(fontName)) {
            return;
        }
        BufferedImage bi = new BufferedImage(SZ, SZ, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bi.createGraphics();
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, SZ, SZ);
        g2d.setColor(Color.black);
        g2d.setFont(font);
        g2d.drawString("1", SZ / 2, SZ / 2);
        int pixCnt1 = countPixels(bi);
        AffineTransform at = AffineTransform.getRotateInstance(Math.PI / 2);
        font = font.deriveFont(Font.PLAIN, at);
        g2d.setFont(font);
        g2d.drawString("1", SZ / 2, SZ / 2);
        int pixCnt2 = countPixels(bi);
        if (args.length > 0) {
            try {
                ImageIO.write(bi, "png", new java.io.File("im.png"));
            } catch (Exception e) {
            }
        }
        if (pixCnt1 == pixCnt2) {
            String msg = "cnt 1 = " + pixCnt1 + " cnt 2 = " + pixCnt2;
            throw new RuntimeException(msg);
        }
    }

    static int countPixels(BufferedImage bi) {
        int cnt = 0;
        int w = bi.getWidth(null);
        int h = bi.getHeight(null);
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < w; j++) {
                int rgb = bi.getRGB(i, j) & 0xFFFFFF;
                if (rgb == 0) {
                    cnt++;
                }
            }
        }
        return cnt;
    }
}
