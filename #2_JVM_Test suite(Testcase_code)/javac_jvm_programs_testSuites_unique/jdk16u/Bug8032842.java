


import java.util.List;
import java.util.Locale;
import java.util.Locale.FilteringMode;
import java.util.Locale.LanguageRange;

public class Bug8032842 {

    public static void main(String[] args) {

        
        
        testFilter("*", List.of("de-CH", "hi-in", "En-GB", "ja-Latn-JP",
                "JA-JP", "en-GB"),
                List.of("de-CH", "hi-in", "En-GB", "ja-Latn-JP", "JA-JP"),
                FilteringMode.AUTOSELECT_FILTERING);

        
        
        testFilter("mtm-RU, en-GB", List.of("En-Gb", "mTm-RU", "en-US",
                "en-latn", "en-GB"),
                List.of("mTm-RU", "En-Gb"), FilteringMode.AUTOSELECT_FILTERING);

        
        
        testFilter("*", List.of("de-CH", "hi-in", "En-GB", "hi-IN",
                "ja-Latn-JP", "JA-JP"),
                List.of("de-CH", "hi-in", "En-GB", "ja-Latn-JP", "JA-JP"),
                FilteringMode.EXTENDED_FILTERING);

        
        
        testFilter("*-ch;q=0.5, *-Latn;q=0.4", List.of("fr-CH", "de-Ch",
                "en-latn", "en-US", "en-Latn"),
                List.of("fr-CH", "de-Ch", "en-latn"),
                FilteringMode.EXTENDED_FILTERING);

        
        testLookup("*-ch;q=0.5", List.of("en", "fR-cH"), "fR-cH");

    }

    public static void testFilter(String ranges, List<String> tags,
            List<String> expected, FilteringMode mode) {
        List<LanguageRange> priorityList = LanguageRange.parse(ranges);
        List<String> actual = Locale.filterTags(priorityList, tags, mode);
        if (!actual.equals(expected)) {
            throw new RuntimeException("[filterTags() failed for the language"
                    + " range: " + ranges + ", Expected: " + expected
                    + ", Found: " + actual + "]");
        }
    }

    public static void testLookup(String ranges, List<String> tags,
            String expected) {
        List<LanguageRange> priorityList = LanguageRange.parse(ranges);
        String actual = Locale.lookupTag(priorityList, tags);
        if (!actual.equals(expected)) {
            throw new RuntimeException("[lookupTag() failed for the language"
                    + " range: " + ranges + ", Expected: " + expected
                    + ", Found: " + actual + "]");
        }
    }

}

