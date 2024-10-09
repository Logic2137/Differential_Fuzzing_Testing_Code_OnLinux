import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public class TestStandardGlyphVectorBug {

    public static void main(String[] args) {
        Font defaultFont = new Font(null);
        FontRenderContext defaultFrc = new FontRenderContext(new AffineTransform(), true, true);
        GlyphVector gv = defaultFont.createGlyphVector(defaultFrc, "test");
        gv.getGlyphLogicalBounds(0);
        Point2D glyphPosition = gv.getGlyphPosition(gv.getNumGlyphs());
        gv.setGlyphPosition(gv.getNumGlyphs(), glyphPosition);
    }
}
