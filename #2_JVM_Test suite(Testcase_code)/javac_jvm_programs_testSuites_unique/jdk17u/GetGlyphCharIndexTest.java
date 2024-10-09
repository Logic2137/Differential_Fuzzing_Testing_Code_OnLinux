import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;

public class GetGlyphCharIndexTest {

    public static void main(String[] args) {
        Font font = new Font(Font.MONOSPACED, Font.PLAIN, 12);
        FontRenderContext frc = new FontRenderContext(null, false, false);
        GlyphVector gv = font.layoutGlyphVector(frc, "abc".toCharArray(), 1, 3, Font.LAYOUT_LEFT_TO_RIGHT);
        int idx0 = gv.getGlyphCharIndex(0);
        if (idx0 != 0) {
            throw new RuntimeException("Expected 0, got " + idx0);
        }
    }
}
