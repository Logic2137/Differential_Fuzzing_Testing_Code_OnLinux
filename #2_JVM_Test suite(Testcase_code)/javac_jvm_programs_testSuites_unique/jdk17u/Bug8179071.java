import java.time.Month;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class Bug8179071 {

    private static final Set<String> LegacyAliases = Set.of("zh-guoyu", "zh-min-nan", "i-klingon", "i-tsu", "sgn-CH-DE", "mo", "i-tay", "scc", "i-hak", "sgn-BE-FR", "i-lux", "tl", "zh-hakka", "i-ami", "aa-SAAHO", "zh-xiang", "i-pwn", "sgn-BE-NL", "jw", "sh", "i-bnn");

    private static final Map<String, String> shortJanuaryNames = Map.of("pa-PK", "\u0a1c\u0a28", "uz-AF", "yan", "sr-ME", "\u0458\u0430\u043d", "scc", "\u0458\u0430\u043d", "sh", "jan", "ha-Latn-NE", "Jan", "i-lux", "Jan.");

    private static void test(String tag, String expected) {
        Locale target = Locale.forLanguageTag(tag);
        Month day = Month.JANUARY;
        TextStyle style = TextStyle.SHORT;
        String actual = day.getDisplayName(style, target);
        if (!actual.equals(expected)) {
            throw new RuntimeException("failed for locale  " + tag + " actual output " + actual + "  does not match with  " + expected);
        }
    }

    private static void checkInvalidTags() {
        Set<String> invalidTags = new HashSet<>();
        Arrays.asList(Locale.getAvailableLocales()).stream().map(loc -> loc.toLanguageTag()).forEach(tag -> {
            if (LegacyAliases.contains(tag)) {
                invalidTags.add(tag);
            }
        });
        if (!invalidTags.isEmpty()) {
            throw new RuntimeException("failed: Deprecated and Legacy tags found  " + invalidTags + " in AvailableLocales ");
        }
    }

    public static void main(String[] args) {
        shortJanuaryNames.forEach(Bug8179071::test);
        checkInvalidTags();
    }
}
