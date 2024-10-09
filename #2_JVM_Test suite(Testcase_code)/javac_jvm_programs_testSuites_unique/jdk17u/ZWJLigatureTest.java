import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Locale;

public class ZWJLigatureTest {

    static final String malayalamName = "Malayalam";

    static final String malayalamFont = "Kartika";

    static final String malayalamText = "\u0D2C\u0D3E\u0D32\u0D28\u0D4D\u200D";

    static final String bengaliName = "Bengali";

    static final String bengaliFont = "Vrinda";

    static final String bengaliText = "\u09CE \u09A4\u09CD\u200D " + "\u09A4\u09BE\u09CE \u09A4\u09BE\u09A4\u09CD\u200D";

    static final String sinhalaName = "Sinhala";

    static final String sinhalaFont = "Iskoola Pota";

    static final String sinhalaText = "\u0DC1\u0DCA\u200D\u0DBB\u0DD3" + "\u0D9A\u0DCA\u200D\u0DBB\u0DD2" + "\u0D9A\u0DCA\u200D\u0DBB\u0DD3" + "\u0DA7\u0DCA\u200D\u0DBB\u0DDA" + "\u0DB6\u0DCA\u200D\u0DBB\u0DD0" + "\u0D9B\u0DCA\u200D\u0DBA\u0DCF";

    static String[] scripts = { malayalamName, bengaliName, sinhalaName };

    static String[] fontNames = { malayalamFont, bengaliFont, sinhalaFont };

    static String[] text = { malayalamText, bengaliText, sinhalaText };

    static void doTest() {
        boolean testFailed = false;
        BufferedImage bi = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = (Graphics2D) bi.getGraphics();
        FontRenderContext frc = g2d.getFontRenderContext();
        for (int f = 0; f < fontNames.length; f++) {
            Font font = new Font(fontNames[f], Font.PLAIN, 30);
            String family = font.getFamily(Locale.ENGLISH).toLowerCase();
            if (!fontNames[f].toLowerCase().equals(family)) {
                System.out.println(fontNames[f] + " not found, skipping.");
                continue;
            } else {
                System.out.println("Testing " + fontNames[f] + " for " + scripts[f]);
            }
            char[] chs = text[f].toCharArray();
            GlyphVector gv = font.layoutGlyphVector(frc, chs, 0, chs.length, 0);
            for (int g = 0; g < gv.getNumGlyphs(); g++) {
                int glyph = gv.getGlyphCode(g);
                int charIdx = gv.getGlyphCharIndex(g);
                int codePoint = text[f].codePointAt(charIdx);
                Point2D pos = gv.getGlyphPosition(g);
                if (codePoint == 0x200D) {
                    testFailed = true;
                    System.out.println("FAIL: GOT ZWJ\n");
                }
                System.out.println("[" + g + "]: gid=" + Integer.toHexString(glyph) + ", charIdx=" + Integer.toHexString(charIdx) + ", codePoint=" + Integer.toHexString(codePoint) + ", pos=[" + pos.getX() + "," + pos.getY() + "]");
            }
        }
        if (testFailed) {
            throw new RuntimeException("TEST FAILED");
        }
    }

    public static void main(String[] args) {
        doTest();
    }
}
