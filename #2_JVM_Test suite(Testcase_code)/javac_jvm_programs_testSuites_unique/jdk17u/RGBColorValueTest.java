import javax.swing.text.AttributeSet;
import javax.swing.text.html.StyleSheet;
import static javax.swing.text.html.CSS.Attribute.*;

public class RGBColorValueTest {

    public static void main(String[] args) {
        StyleSheet styleSheet = new StyleSheet();
        AttributeSet attributeSet = styleSheet.getDeclaration("border-color: rgb(1, 2, 3)    rgb(1, 2, 4);");
        if (!attributeSet.getAttribute(BORDER_TOP_COLOR).toString().equals("rgb(1, 2, 3)") || !attributeSet.getAttribute(BORDER_BOTTOM_COLOR).toString().equals("rgb(1, 2, 3)") || !attributeSet.getAttribute(BORDER_RIGHT_COLOR).toString().equals("rgb(1, 2, 4)") || !attributeSet.getAttribute(BORDER_LEFT_COLOR).toString().equals("rgb(1, 2, 4)")) {
            throw new RuntimeException("Failed");
        }
    }
}
