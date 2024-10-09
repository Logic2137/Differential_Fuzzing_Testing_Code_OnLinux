import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;

public class FontSize1Test {

    static final String text = "abcdefghijklmnopqrstuvwxyz";

    public static void main(String[] args) {
        BufferedImage bi = new BufferedImage(100, 20, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bi.createGraphics();
        Font[] af = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
        for (Font f : af) {
            System.out.println("Looking at font " + f);
            g2d.setFont(f);
            g2d.getFontMetrics().getWidths();
            g2d.drawString(text, 50, 10);
        }
        g2d.dispose();
    }
}
