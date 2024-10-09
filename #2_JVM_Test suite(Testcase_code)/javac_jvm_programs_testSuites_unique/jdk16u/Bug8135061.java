



import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Locale.LanguageRange;

public class Bug8135061 {

    public static void main(String[] args) {

        
        List<LanguageRange> ranges = LanguageRange.parse("nv");
        Collection<Locale> locales = Collections.singleton(Locale.ENGLISH);

        try {
            Locale match = Locale.lookup(ranges, locales);
            if (match != null) {
                throw new RuntimeException("Locale.lookup returned non-null: "
                        + match);
            }
        } catch (Exception ex) {
            throw new RuntimeException("[Locale.lookup failed on language"
                    + " range: " + ranges + " and language tags "
                    + locales + "]", ex);
        }

        
        ranges = LanguageRange.parse("i-navajo");
        locales = Collections.singleton(new Locale("nv"));

        try {
            Locale match = Locale.lookup(ranges, locales);
            if (!match.toLanguageTag().equals("nv")) {
                throw new RuntimeException("Locale.lookup returned unexpected"
                        + " result: " + match);
            }
        } catch (Exception ex) {
            throw new RuntimeException("[Locale.lookup failed on language"
                    + " range: " + ranges + " and language tags "
                    + locales + "]", ex);
        }

    }

}
