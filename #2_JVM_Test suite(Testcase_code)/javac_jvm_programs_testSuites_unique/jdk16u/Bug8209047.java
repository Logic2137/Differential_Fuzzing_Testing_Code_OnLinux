
 
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.Set;

public class Bug8209047 {

    public static void main(String[] args) {
        Set<Integer> styles = Set.of(DateFormat.FULL, DateFormat.LONG, DateFormat.MEDIUM, DateFormat.SHORT);
        Arrays.stream(Locale.getAvailableLocales()).forEach(locale -> {
            styles.forEach(style -> {
                try {
                    DateFormat.getDateInstance(style, locale);
                    DateFormat.getTimeInstance(style, locale);
                    DateFormat.getDateTimeInstance(style, style, locale);
                } catch (IllegalArgumentException ex) {
                    throw new RuntimeException("Getting DateFormat instance failed for locale " + locale, ex);
                }
            });
        });
    }
}
