import java.awt.*;
import java.awt.image.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.text.*;
import java.util.Hashtable;

public class AllFontsLBM {

    public static void main(String[] args) {
        Font[] allFonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
        for (int i = 0; i < allFonts.length; i++) {
            try {
                Font f = allFonts[i].deriveFont(Font.PLAIN, 20);
                if (f.getFontName().startsWith("HiraKaku")) {
                    continue;
                }
                System.out.println("Try : " + f.getFontName());
                System.out.flush();
                breakLines(f);
            } catch (Exception e) {
                System.out.println(allFonts[i]);
            }
        }
    }

    static void breakLines(Font font) {
        AttributedString vanGogh = new AttributedString("Many people believe that Vincent van Gogh painted his best works " + "during the two-year period he spent in Provence. Here is where he " + "painted The Starry Night--which some consider to be his greatest " + "work of all. However, as his artistic brilliance reached new " + "heights in Provence, his physical and mental health plummeted. ", new Hashtable());
        vanGogh.addAttribute(TextAttribute.FONT, font);
        BufferedImage bi = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bi.createGraphics();
        AttributedCharacterIterator aci = vanGogh.getIterator();
        FontRenderContext frc = new FontRenderContext(null, false, false);
        LineBreakMeasurer lbm = new LineBreakMeasurer(aci, frc);
        lbm.setPosition(aci.getBeginIndex());
        while (lbm.getPosition() < aci.getEndIndex()) {
            lbm.nextLayout(100f);
        }
    }
}
