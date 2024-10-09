

 



import java.time.Month;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class Bug8179071 {

    
    private static final Set<String> LegacyAliases = Set.of("pa-PK", "ug-Arab-CN", "kk-Cyrl-KZ",
            "bs-BA", "ks-Arab-IN", "mn-Cyrl-MN", "ha-Latn-NE",
            "shi-MA", "ha-Latn-NG", "ms-Latn-BN","ms-Latn-SG",
            "ky-Cyrl-KG", "az-AZ", "zh-guoyu", "zh-min-nan", "i-klingon", "i-tsu",
            "sr-XK", "sgn-CH-DE", "mo", "i-tay", "scc", "uz-UZ", "uz-AF", "sr-RS",
            "i-hak", "sgn-BE-FR", "i-lux", "vai-LR", "tl", "zh-hakka", "i-ami", "aa-SAAHO", "ha-Latn-GH",
            "zh-xiang", "i-pwn", "sgn-BE-NL", "jw", "sh", "tzm-Latn-MA", "i-bnn");
    
    private static Map<String, String> shortJanuaryNames = Map.of( "pa-PK", "\u062c\u0646\u0648\u0631\u06cc",
                                                          "uz-AF" , "\u062c\u0646\u0648",
                                                          "sr-ME", "jan.",
                                                          "scc", "\u0458\u0430\u043d",
                                                          "sh", "jan",
                                                          "ha-Latn-NE", "Jan",
                                                          "i-lux", "Jan.");


    private static void test(String tag, String expected) {
        Locale target = Locale.forLanguageTag(tag);
        Month day = Month.JANUARY;
        TextStyle style = TextStyle.SHORT;
        String actual = day.getDisplayName(style, target);
        if (!actual.equals(expected)) {
            throw new RuntimeException("failed for locale  " + tag + " actual output " + actual +"  does not match with  " + expected);
        }
    }

    
    private static void checkInvalidTags() {
        Set<String> invalidTags = new HashSet<>();
        Arrays.asList(Locale.getAvailableLocales()).stream()
                .map(loc -> loc.toLanguageTag())
                .forEach( tag -> {if(LegacyAliases.contains(tag)) {invalidTags.add(tag);}});
        if (!invalidTags.isEmpty()) {
          throw new RuntimeException("failed: Deprecated and Legacy tags found  " + invalidTags  + " in AvailableLocales ");
        }
    }

    public static void main(String[] args) {
        shortJanuaryNames.forEach((key, value) -> test(key, value));
        checkInvalidTags();
    }
}
