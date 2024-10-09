


import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.font.FontRenderContext;

public class MonospacedGlyphWidthTest {
    private static final int ASCII_START_INDEX = 0x0061;
    private static final int ASCII_END_INDEX = 0x007A;

    private static final int TEST_START_INDEX = 0x2018;
    private static final int TEST_END_INDEX = 0x201F;

    private static boolean checkChars(int start, int end, boolean except) {
        Font font = new Font(Font.MONOSPACED, Font.PLAIN, 12);
        double width = getCharWidth(font, 'a');

        for (int i = start; i <= end; i++) {
            if (!(font.canDisplay(i))) {
                if (except) {
                    continue;
                } else {
                    return false;
                }
            }
            if (width != getCharWidth(font, (char)i)) {
                if (except) {
                    throw new RuntimeException(
                          "Test Failed: characters have different width!");
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    private static double getCharWidth(Font font, char c) {
        FontRenderContext fontRenderContext = new FontRenderContext(null, false, false);
        return font.getStringBounds(new char[] {c}, 0, 1, fontRenderContext).getWidth();
    }

    public static void main(String[] args) {
        if (!checkChars(ASCII_START_INDEX, ASCII_END_INDEX, false)) {
           System.out.println("It appears there are no suitable fonts");
           System.out.println("Here are the fonts found on this system:");
           GraphicsEnvironment ge =
               GraphicsEnvironment.getLocalGraphicsEnvironment();
           Font[] fonts = ge.getAllFonts();
           for (Font f : fonts) {
               System.out.println(f);
           }

           return;
        }

        checkChars(TEST_START_INDEX, TEST_END_INDEX, true);
        System.out.println("Test Passed!");
    }
}
