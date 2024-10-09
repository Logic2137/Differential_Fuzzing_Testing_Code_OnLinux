import java.util.List;
import java.util.Locale;
import java.util.Locale.LanguageRange;
import java.util.Locale.FilteringMode;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
import static java.util.Locale.FilteringMode.EXTENDED_FILTERING;
import static java.util.Locale.FilteringMode.AUTOSELECT_FILTERING;

public class Bug8159420 {

    static boolean err = false;

    public static void main(String[] args) {
        Locale origLocale = null;
        try {
            origLocale = Locale.getDefault();
            Locale.setDefault(new Locale("tr", "TR"));
            testParse();
            testFilter(EXTENDED_FILTERING);
            testFilter(AUTOSELECT_FILTERING);
            testLookup();
            testMapEquivalents();
            if (err) {
                throw new RuntimeException("[LocaleMatcher method(s) in turkish" + " locale failed]");
            }
        } finally {
            Locale.setDefault(origLocale);
        }
    }

    private static void testParse() {
        String ranges = "HI-Deva, ja-hIrA-JP, RKI";
        try {
            LanguageRange.parse(ranges);
        } catch (Exception ex) {
            System.err.println("[testParse() failed on range string: " + ranges + "] due to " + ex);
            err = true;
        }
    }

    private static void testFilter(FilteringMode mode) {
        String ranges = "hi-IN, itc-Ital";
        String tags = "hi-IN, itc-Ital";
        List<LanguageRange> priorityList = LanguageRange.parse(ranges);
        List<Locale> tagList = generateLocales(tags);
        String actualLocales = showLocales(Locale.filter(priorityList, tagList, mode));
        String expectedLocales = "hi-IN, itc-Ital";
        if (!expectedLocales.equals(actualLocales)) {
            System.err.println("testFilter(" + mode + ") failed on language ranges:" + " [" + ranges + "] and language tags: [" + tags + "]");
            err = true;
        }
    }

    private static void testLookup() {
        boolean error = false;
        String ranges = "hi-IN, itc-Ital";
        String tags = "hi-IN, itc-Ital";
        List<LanguageRange> priorityList = LanguageRange.parse(ranges);
        List<Locale> localeList = generateLocales(tags);
        Locale actualLocale = Locale.lookup(priorityList, localeList);
        String actualLocaleString = "";
        if (actualLocale != null) {
            actualLocaleString = actualLocale.toLanguageTag();
        } else {
            error = true;
        }
        String expectedLocale = "hi-IN";
        if (!expectedLocale.equals(actualLocaleString)) {
            error = true;
        }
        if (error) {
            System.err.println("testLookup() failed on language ranges:" + " [" + ranges + "] and language tags: [" + tags + "]");
            err = true;
        }
    }

    private static void testMapEquivalents() {
        String ranges = "HI-IN";
        List<LanguageRange> priorityList = LanguageRange.parse(ranges);
        HashMap<String, List<String>> map = new LinkedHashMap<>();
        List<String> equivalentList = new ArrayList<>();
        equivalentList.add("HI");
        equivalentList.add("HI-Deva");
        map.put("HI", equivalentList);
        List<LanguageRange> expected = new ArrayList<>();
        expected.add(new LanguageRange("hi-in"));
        expected.add(new LanguageRange("hi-deva-in"));
        List<LanguageRange> got = LanguageRange.mapEquivalents(priorityList, map);
        if (!areEqual(expected, got)) {
            System.err.println("testMapEquivalents() failed");
            err = true;
        }
    }

    private static boolean areEqual(List<LanguageRange> expected, List<LanguageRange> got) {
        boolean error = false;
        if (expected.equals(got)) {
            return !error;
        }
        List<LanguageRange> cloneExpected = new ArrayList<>(expected);
        cloneExpected.removeAll(got);
        if (!cloneExpected.isEmpty()) {
            error = true;
            System.err.println("Found missing range(s): " + cloneExpected);
        }
        got.removeAll(expected);
        if (!got.isEmpty()) {
            error = true;
            System.err.println("Found extra range(s): " + got);
        }
        return !error;
    }

    private static List<Locale> generateLocales(String tags) {
        if (tags == null) {
            return null;
        }
        List<Locale> localeList = new ArrayList<>();
        if (tags.equals("")) {
            return localeList;
        }
        String[] t = tags.split(", ");
        for (String tag : t) {
            localeList.add(Locale.forLanguageTag(tag));
        }
        return localeList;
    }

    private static String showLocales(List<Locale> locales) {
        StringBuilder sb = new StringBuilder();
        Iterator<Locale> itr = locales.iterator();
        if (itr.hasNext()) {
            sb.append(itr.next().toLanguageTag());
        }
        while (itr.hasNext()) {
            sb.append(", ");
            sb.append(itr.next().toLanguageTag());
        }
        return sb.toString().trim();
    }
}
