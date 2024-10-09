


import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class Bug8166994 {

    public static void main(String[] args) {
        List<String> list = Arrays.asList("ccq-aa", "ybd-aa", "rki-aa");
        String ranges = "ccq-aa";
        testParseConsistency(list, ranges);

        
        testParseConsistency(list, ranges);

        
        
        list = Arrays.asList("gfx-xz", "oun-xz", "mwj-xz", "vaj-xz",
                "taj-xy", "tsf-xy");
        ranges = "gfx-xz, taj-xy";
        testParseConsistency(list, ranges);
        
        testParseConsistency(list, ranges);

    }

    private static void testParseConsistency(List<String> list, String ranges) {
        List<String> priorityList = parseRanges(ranges);
        if (!list.equals(priorityList)) {
            throw new RuntimeException("Failed to parse the language range ["
                    + ranges + "], Expected: " + list + " Found: "
                    + priorityList);
        }
    }

    private static List<String> parseRanges(String s) {
        return Locale.LanguageRange.parse(s).stream()
                .map(Locale.LanguageRange::getRange)
                .collect(Collectors.toList());
    }

}

