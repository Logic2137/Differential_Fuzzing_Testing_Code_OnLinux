import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.text.AttributedCharacterIterator;
import java.util.HashMap;

public class ZeroWidthStringBoundsTest {

    public static void main(String[] args) {
        FontRenderContext frc = new FontRenderContext(null, false, false);
        Font f1 = new Font(Font.MONOSPACED, Font.PLAIN, 12);
        f1.getStringBounds("", frc);
        HashMap<AttributedCharacterIterator.Attribute, Object> attrs = new HashMap<>();
        attrs.put(TextAttribute.KERNING, TextAttribute.KERNING_ON);
        Font f2 = f1.deriveFont(attrs);
        f2.getStringBounds("", frc);
    }
}
