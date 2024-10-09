



import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;

public class NLGlyphTest {

   public static void main(String[] args) {
      char[] chs = { '\n' };
      FontRenderContext frc = new FontRenderContext(null, true, true);
      GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
      Font[] fonts = ge.getAllFonts();
      for (Font font : fonts) {
          GlyphVector cgv = font.createGlyphVector(frc, "\n");
          GlyphVector lgv = font.layoutGlyphVector(frc, chs, 0, 1, 0);
          int c_code = cgv.getGlyphCode(0);
          int l_code = lgv.getGlyphCode(0);
          if ((c_code != l_code) || (l_code == 0)) {
              System.out.println(font);
              System.out.println("create code="+c_code + " layout code="+l_code);
              Rectangle r_l = lgv.getPixelBounds(frc, 0f, 0f);
              Rectangle r_c = cgv.getPixelBounds(frc, 0f, 0f);
              System.out.println(r_l.isEmpty()+" "+ r_c.isEmpty());
              if (r_l.isEmpty() != r_c.isEmpty()) {
                 throw new RuntimeException("One glyph renders");
              }
          }
      }
   }
}
