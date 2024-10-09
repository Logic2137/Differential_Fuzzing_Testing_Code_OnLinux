import javax.swing.JComponent;
import java.util.Locale;

public class TestNullLocale {

    public static void main(String[] args) {
        Locale defaultLocale = JComponent.getDefaultLocale();
        JComponent.setDefaultLocale(Locale.GERMAN);
        JComponent.setDefaultLocale(null);
        Locale currentLocale = JComponent.getDefaultLocale();
        if (defaultLocale != currentLocale) {
            System.out.println("currentLocale " + currentLocale);
            System.out.println("defaultLocale " + defaultLocale);
            throw new RuntimeException("locale not reset to default locale");
        }
    }
}
