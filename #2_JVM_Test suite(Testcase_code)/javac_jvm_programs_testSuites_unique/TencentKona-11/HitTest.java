



import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.font.TextHitInfo;
import java.awt.font.TextLayout;

public class HitTest {

  public static void main(String args[]) {
      String s = new String(new int[]{0x1d400, 0x61}, 0, 2);
      Font font = new Font("Dialog", Font.PLAIN, 12);
      FontRenderContext frc = new FontRenderContext(null, false, false);
      TextLayout tl = new TextLayout(s, font, frc);
      TextHitInfo currHit = TextHitInfo.beforeOffset(3);
      TextHitInfo prevHit = tl.getNextLeftHit(currHit);
      System.out.println("index=" + prevHit.getCharIndex()+
                         " leading edge=" + prevHit.isLeadingEdge());
      if (prevHit.getCharIndex() != 2) {
          throw new RuntimeException("Expected 2 for hit index");
      }
   }
}
