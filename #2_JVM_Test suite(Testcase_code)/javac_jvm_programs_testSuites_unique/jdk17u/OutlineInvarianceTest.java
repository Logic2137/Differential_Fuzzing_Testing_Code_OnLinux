import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.image.*;

public class OutlineInvarianceTest {

    private static Shape getOutline(Font font, String str, boolean isAA) {
        BufferedImage img = new BufferedImage(400, 400, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gc = (Graphics2D) img.getGraphics();
        if (isAA) {
            gc.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        } else {
            gc.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        }
        FontRenderContext frc = gc.getFontRenderContext();
        GlyphVector gv;
        gv = font.layoutGlyphVector(frc, str.toCharArray(), 0, str.length(), 0);
        return gv.getOutline();
    }

    private static boolean compareShapes(Shape s1, Shape s2) {
        if (s1 == null || s2 == null)
            return false;
        PathIterator p1 = s1.getPathIterator(null);
        float[] coords1 = { 0f, 0f, 0f, 0f, 0f, 0f };
        PathIterator p2 = s2.getPathIterator(null);
        float[] coords2 = { 0f, 0f, 0f, 0f, 0f, 0f };
        while (!p1.isDone() || !p2.isDone()) {
            if (p1.currentSegment(coords1) != p2.currentSegment(coords2)) {
                System.err.println("Types of segment are different");
                return false;
            }
            for (int i = 0; i < 6; i++) if (coords1[i] != coords2[i]) {
                System.err.println("Coordinate " + i + " are different");
                return false;
            }
            p1.next();
            p2.next();
        }
        if (!(p1.isDone() && p2.isDone())) {
            System.err.println("Length of paths are different");
        }
        return (p1.isDone() && p2.isDone());
    }

    private static void testFont(String family, float sz, String testStr) throws Exception {
        Font f = new Font(family, Font.PLAIN, 1);
        for (int style = 1; style < 4; style++) {
            f = f.deriveFont(Font.BOLD, sz);
            Shape s1 = getOutline(f, testStr, false);
            Shape s2 = getOutline(f, testStr, true);
            if (!compareShapes(s1, s2))
                throw new Exception("Failed for font " + f);
        }
    }

    public static void main(String[] arg) throws Exception {
        String testStr = "0123456789";
        testFont("MS Gothic", 30.0f, testStr);
        testFont("MS Gothic", 12.0f, testStr);
        testFont("MS Gothic", 7.0f, testStr);
    }
}
