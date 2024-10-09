


import java.awt.*;
import java.awt.font.FontRenderContext;

public class MonospacedGlyphWidthTest {
    private static final int START_INDEX = 0x2018;
    private static final int END_INDEX = 0x201F;

    public static void main(String[] args) {
        Font font = new Font(Font.MONOSPACED, Font.PLAIN, 12);
        double width = getCharWidth(font, 'a');

        for (int i = START_INDEX; i <= END_INDEX; i++) {
            if (width != getCharWidth(font, (char)i)) {
                throw new RuntimeException("Test Failed: characters have different width!");
            }
        }
        System.out.println("Test Passed!");
    }

    private static double getCharWidth(Font font, char c) {
        FontRenderContext fontRenderContext = new FontRenderContext(null, false, false);
        return font.getStringBounds(new char[] {c}, 0, 1, fontRenderContext).getWidth();
    }
}

