




import java.awt.Font;
import java.util.Locale;

public class TrueTypeFontLocaleNameTest {

    public static void main(String[] args) {

        String os = System.getProperty("os.name", "");
        if (!os.toLowerCase().startsWith("win")) {
            return;
        }
        System.setProperty("user.language", "de");
        System.setProperty("user.country", "AT");
        Locale de_atLocale = new Locale("de", "AT");
        Locale.setDefault(de_atLocale);

        String family = "Verdana";
        Font font = new Font(family, Font.BOLD, 12);
        if (!font.getFamily(Locale.ENGLISH).equals(family)) {
            System.out.println(family + " not found - skipping test.");
            return;
        }

        String atFontName = font.getFontName();
        Locale deGELocale = new Locale("de", "GE");
        String deFontName = font.getFontName(deGELocale);
        System.out.println("Austrian font name: " + atFontName);
        System.out.println("German font name: " + deFontName);

        String deLangFullName = "Verdana Fett";
        
        
        
        if (!deFontName.equals(atFontName)) {
            throw new RuntimeException("Font names differ " +
                                       deFontName + " " + atFontName);
        }
        if (!deLangFullName.equals(deFontName)) {
            throw new RuntimeException("Font name is not " + deLangFullName +
                                       " instead got " + deFontName);
        }
    }
}
