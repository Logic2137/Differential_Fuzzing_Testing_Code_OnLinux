import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.Arrays;

public class MyanmarFallbackTest {

    public static void main(String[] args) {
        if (!System.getProperty("os.name").toLowerCase().contains("windows")) {
            System.err.println("This test is for Windows only");
            return;
        }
        String[] fontFamilyNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        if (Arrays.stream(fontFamilyNames).noneMatch("Myanmar Text"::equals)) {
            System.err.println("Myanmar Text font is not installed");
            return;
        }
        Font dialog = new Font(Font.DIALOG, Font.PLAIN, 12);
        if (-1 != dialog.canDisplayUpTo("\u1000\u103C")) {
            throw new RuntimeException("Cannot display Myanmar characters");
        }
    }
}
