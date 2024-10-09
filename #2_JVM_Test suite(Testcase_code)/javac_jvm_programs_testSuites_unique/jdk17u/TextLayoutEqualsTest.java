import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;

public class TextLayoutEqualsTest {

    public static void main(String[] args) {
        Font font = new Font(Font.DIALOG, Font.PLAIN, 12);
        String text = "hello world";
        FontRenderContext frc = new FontRenderContext(null, false, false);
        TextLayout tl1 = new TextLayout(text, font, frc);
        TextLayout tl2 = new TextLayout(text, font, frc);
        if (tl1.equals(tl2) || tl2.equals(tl1) || tl1.equals((Object) tl2) || tl2.equals((Object) tl1)) {
            throw new RuntimeException("Equal TextLayouts");
        }
        if (!tl1.equals(tl1) || !tl1.equals((Object) tl1)) {
            throw new RuntimeException("Non-Equal TextLayouts");
        }
    }
}
