





import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.font.TextHitInfo;
import java.awt.font.FontRenderContext;
import java.util.Hashtable;



public class LigatureCaretTest {

    public static void main(String[] args) {

        
        testLamAlef();
        System.out.println("LigatureCaretTest PASSED");
    }

    
    private static final Hashtable map = new Hashtable();
    static {
      map.put(TextAttribute.FONT, new Font("Lucida Sans", Font.PLAIN, 24));
    }
    private static final FontRenderContext frc =
                                new FontRenderContext(null, false, false);

    
    public static void testBidiWithNumbers() {

        String bidiWithNumbers = "abc\u05D0\u05D1\u05D2123abc";
        
        

        int[] carets = { 0, 1, 2, 3, 7, 8, 6, 5, 4, 9, 10, 11, 12 };
        TextLayout layout = new TextLayout(bidiWithNumbers, map, frc);

        
        for (int i=0; i < carets.length-1; i++) {

            TextHitInfo hit = layout.getNextRightHit(carets[i]);
            if (hit.getInsertionIndex() != carets[i+1]) {
                throw new Error("right hit failed within layout");
            }
        }

        if (layout.getNextRightHit(carets[carets.length-1]) != null) {
            throw new Error("right hit failed at end of layout");
        }

        for (int i=carets.length-1; i > 0; i--) {

            TextHitInfo hit = layout.getNextLeftHit(carets[i]);
            if (hit.getInsertionIndex() != carets[i-1]) {
                throw new Error("left hit failed within layout");
            }
        }

        if (layout.getNextLeftHit(carets[0]) != null) {
            throw new Error("left hit failed at end of layout");
        }
    }

    
    public static void testLamAlef() {

        
        final String lamAlef = "\u0644\u0627";
        final String ltrText = "abcd";

        
        
        
        TextLayout layout = new TextLayout(lamAlef, map, frc);

        TextHitInfo hit;

        hit = layout.getNextLeftHit(0);
        if (hit.getInsertionIndex() != 2) {
            throw new Error("Left hit failed.  Hit:" + hit);
        }

        hit = layout.getNextRightHit(2);
        if (hit.getInsertionIndex() != 0) {
            throw new Error("Right hit failed.  Hit:" + hit);
        }

        hit = layout.hitTestChar(layout.getAdvance()/2, 0);
        if (hit.getInsertionIndex() != 0 && hit.getInsertionIndex() != 2) {
            throw new Error("Hit-test allowed incorrect caret.  Hit:" + hit);
        }


        
        
        
        layout = new TextLayout(ltrText+lamAlef, map, frc);

        final int ltrLen = ltrText.length();
        final int layoutLen = layout.getCharacterCount();

        for (int i=0; i < ltrLen; i++) {
            hit = layout.getNextRightHit(i);
            if (hit.getInsertionIndex() != i+1) {
                throw new Error("Right hit failed in ltr text.");
            }
        }

        hit = layout.getNextRightHit(ltrLen);
        if (layoutLen != hit.getInsertionIndex()) {
            throw new Error("Right hit failed at direction boundary.");
        }

        hit = layout.getNextLeftHit(layoutLen);
        if (hit.getInsertionIndex() != ltrLen) {
            throw new Error("Left hit failed at end of text.");
        }
    }
}
