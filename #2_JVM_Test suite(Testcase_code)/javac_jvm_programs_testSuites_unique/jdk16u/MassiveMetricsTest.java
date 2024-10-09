



import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;

public class MassiveMetricsTest {

    public static void main(String [] args) {

        GraphicsEnvironment ge =
            GraphicsEnvironment.getLocalGraphicsEnvironment();
        Font[] fonts = ge.getAllFonts();
        BufferedImage bi = new BufferedImage(1,1,1);
        Graphics2D g2d = bi.createGraphics();
        int[] sizes = { 80, 100, 120, 600, 1600, 2400, 3600, 7200, 12000 };
        String s = "m";

        for (Font f : fonts) {
            Font sz12Font = f.deriveFont(Font.PLAIN, 12);
            FontMetrics sz12 = g2d.getFontMetrics(sz12Font);
            if (sz12.stringWidth(s) == 0) {
                continue; 
            }
            boolean fail = false;
            for (int sz : sizes) {
                Font font = f.deriveFont(Font.PLAIN, sz);
                FontMetrics fm = g2d.getFontMetrics(font);
                if (fm.stringWidth(s) == 0) {
                  fail = true;
                  System.err.println("zero for " + font);
                }
            }
            if (fail) {
                throw new RuntimeException("Zero stringwidth");
            }
        }
    }
}
