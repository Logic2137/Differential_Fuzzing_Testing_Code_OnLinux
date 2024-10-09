import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class SpaceAdvance {

    public static void main(String[] args) throws Exception {
        BufferedImage bi = new BufferedImage(1, 1, 1);
        Graphics2D g2d = bi.createGraphics();
        Font font = new Font(Font.DIALOG, Font.PLAIN, 12);
        if (!font.canDisplay(' ')) {
            return;
        }
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        if (fm.charWidth(' ') == 0) {
            throw new RuntimeException("Space has char width of 0");
        }
    }
}
