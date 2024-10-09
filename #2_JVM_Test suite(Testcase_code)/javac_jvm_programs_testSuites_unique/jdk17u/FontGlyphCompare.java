import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class FontGlyphCompare {

    static BufferedImage getFontImage(Font font, String text) {
        int x = 1;
        int y = 15;
        int w = 10;
        int h = 18;
        BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) bi.getGraphics();
        g.setColor(Color.black);
        g.fillRect(0, 0, w, h);
        g.setColor(Color.white);
        g.setFont(font);
        g.drawString(text, x, y);
        return bi;
    }

    public static void main(String[] args) throws Exception {
        String osName = System.getProperty("os.name");
        System.out.println("OS is " + osName);
        osName = osName.toLowerCase();
        if (!osName.startsWith("windows")) {
            return;
        }
        Font msMincho = new Font("MS Mincho", Font.PLAIN, 16);
        String family = msMincho.getFamily(java.util.Locale.ENGLISH);
        if (!family.equalsIgnoreCase("MS Mincho")) {
            System.out.println("Japanese fonts not installed");
            return;
        }
        String s = "|";
        BufferedImage bi1 = getFontImage(new Font("MS Mincho", Font.PLAIN, 16), s);
        int h1 = bi1.getHeight();
        int w1 = bi1.getWidth();
        BufferedImage bi2 = getFontImage(new Font("MS Mincho", Font.ITALIC, 16), s);
        int h2 = bi2.getHeight();
        int w2 = bi2.getWidth();
        if ((h1 == h2) && (w1 == w2)) {
            int cnt = 0;
            for (int yy = 0; yy < h1; yy++) {
                for (int xx = 0; xx < w1; xx++) {
                    if (bi1.getRGB(xx, yy) != bi2.getRGB(xx, yy)) {
                        cnt++;
                    }
                }
            }
            if (cnt == 0) {
                throw new Exception("Test failed");
            }
        }
    }
}
