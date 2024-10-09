




import java.awt.*;
import java.awt.event.*;
import java.awt.font.*;
import java.awt.geom.*;

public class TestLayoutFlags {

    static public void main(String[] args) {
        new TestLayoutFlags().runTest();
    }

    void runTest() {

        Font font = new Font("Lucida Sans", Font.PLAIN, 24);

        String latin1 = "This is a latin1 string"; 
        String hebrew = "\u05d0\u05d1\u05d2\u05d3"; 
        String arabic = "\u0646\u0644\u0622\u0646"; 
        String hindi = "\u0939\u093f\u0923\u094d\u0921\u0940"; 
        

        FontRenderContext frc = new FontRenderContext(null, true, true);

        
        {
          GlyphVector gv = font.createGlyphVector(frc, "abcde");
          int ix = gv.getGlyphCharIndex(0);
          if (ix != 0) {
            throw new Error("glyph 0 incorrectly mapped to char " + ix);
          }
          int[] ixs = gv.getGlyphCharIndices(0, gv.getNumGlyphs(), null);
          for (int i = 0; i < ixs.length; ++i) {
            if (ixs[i] != i) {
              throw new Error("glyph " + i + " incorrectly mapped to char " + ixs[i]);
            }
          }
        }

        GlyphVector latinGV = makeGlyphVector("Lucida Sans", frc, latin1, false, 1 );
        GlyphVector hebrewGV = makeGlyphVector("Lucida Sans", frc, hebrew, true, 5 );
        GlyphVector arabicGV = makeGlyphVector("Lucida Sans", frc, arabic, true, 6 );
        GlyphVector hindiGV = makeGlyphVector("Lucida Sans", frc, hindi, false, 7 );
        

        GlyphVector latinPos = font.createGlyphVector(frc, latin1);
        Point2D pt = latinPos.getGlyphPosition(0);
        pt.setLocation(pt.getX(), pt.getY() + 1.0);
        latinPos.setGlyphPosition(0, pt);

        GlyphVector latinTrans = font.createGlyphVector(frc, latin1);
        latinTrans.setGlyphTransform(0, AffineTransform.getRotateInstance(.15));

        test("latin", latinGV, GlyphVector.FLAG_HAS_POSITION_ADJUSTMENTS);
        test("hebrew", hebrewGV, GlyphVector.FLAG_RUN_RTL |
             GlyphVector.FLAG_HAS_POSITION_ADJUSTMENTS);
        test("arabic", arabicGV, GlyphVector.FLAG_COMPLEX_GLYPHS |
             GlyphVector.FLAG_HAS_POSITION_ADJUSTMENTS);
        test("hindi", hindiGV, GlyphVector.FLAG_COMPLEX_GLYPHS |
             GlyphVector.FLAG_HAS_POSITION_ADJUSTMENTS);
        
        test("pos", latinPos, GlyphVector.FLAG_HAS_POSITION_ADJUSTMENTS);
        test("trans", latinTrans, GlyphVector.FLAG_HAS_TRANSFORMS);
    }

    GlyphVector makeGlyphVector(String fontname, FontRenderContext frc, String text, boolean rtl, int script) {
        Font font = new Font(fontname, Font.PLAIN, 14);
        System.out.println("asking for " + fontname + " and got " + font.getFontName());
        int flags = rtl ? 1 : 0;
        return font.layoutGlyphVector(frc, text.toCharArray(), 0, text.length(), flags);
    }

    void test(String name, GlyphVector gv, int expectedFlags) {
        expectedFlags &= gv.FLAG_MASK;
        int computedFlags = computeFlags(gv) & gv.FLAG_MASK;
        int actualFlags = gv.getLayoutFlags() & gv.FLAG_MASK;

        System.out.println("\n*** " + name + " ***");
        System.out.println(" test flags");
        System.out.print("expected ");
        printFlags(expectedFlags);
        System.out.print("computed ");
        printFlags(computedFlags);
        System.out.print("  actual ");
        printFlags(actualFlags);

        if (expectedFlags != actualFlags) {
            throw new Error("layout flags in test: " + name +
                            " expected: " + Integer.toHexString(expectedFlags) +
                            " but got: " + Integer.toHexString(actualFlags));
        }
    }

    static public void printFlags(int flags) {
        System.out.print("flags:");
        if ((flags & GlyphVector.FLAG_HAS_POSITION_ADJUSTMENTS) != 0) {
            System.out.print(" pos");
        }
        if ((flags & GlyphVector.FLAG_HAS_TRANSFORMS) != 0) {
            System.out.print(" trans");
        }
        if ((flags & GlyphVector.FLAG_RUN_RTL) != 0) {
            System.out.print(" rtl");
        }
        if ((flags & GlyphVector.FLAG_COMPLEX_GLYPHS) != 0) {
            System.out.print(" complex");
        }
        if ((flags & GlyphVector.FLAG_MASK) == 0) {
            System.out.print(" none");
        }
        System.out.println();
    }

    int computeFlags(GlyphVector gv) {
        validateCharIndexMethods(gv);

        int result = 0;
        if (glyphsAreRTL(gv)) {
            result |= GlyphVector.FLAG_RUN_RTL;
        }
        if (hasComplexGlyphs(gv)) {
            result |= GlyphVector.FLAG_COMPLEX_GLYPHS;
        }

        return result;
    }

    
    void validateCharIndexMethods(GlyphVector gv) {
        int[] indices = gv.getGlyphCharIndices(0, gv.getNumGlyphs(), null);
        for (int i = 0; i < gv.getNumGlyphs(); ++i) {
            if (gv.getGlyphCharIndex(i) != indices[i]) {
                throw new Error("glyph index mismatch at " + i);
            }
        }
    }

    
    boolean glyphsAreLTR(GlyphVector gv) {
        int[] indices = gv.getGlyphCharIndices(0, gv.getNumGlyphs(), null);
        for (int i = 0; i < indices.length; ++i) {
            if (indices[i] != i) {
                return false;
            }
        }
        return true;
    }

    
    boolean glyphsAreRTL(GlyphVector gv) {
        int[] indices = gv.getGlyphCharIndices(0, gv.getNumGlyphs(), null);
        for (int i = 0; i < indices.length; ++i) {
            if (indices[i] != indices.length - i - 1) {
                return false;
            }
        }
        return true;
    }

    
    boolean hasComplexGlyphs(GlyphVector gv) {
        return !glyphsAreLTR(gv) && !glyphsAreRTL(gv);
    }
}


