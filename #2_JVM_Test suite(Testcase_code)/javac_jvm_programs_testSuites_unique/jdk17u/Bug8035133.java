import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class Bug8035133 {

    private static boolean err = false;

    public static void main(String[] args) {
        checkLookup("en;q=0.1, *-ch;q=0.5, de-ch;q=0", "de-ch, en, fr-ch", "fr-CH");
        checkLookup("en;q=0.1, *-ch;q=0.5, *;q=0", "de-ch, en, fr-ch", "de-CH");
        checkFilter("en;q=0.1, fr-ch;q=0.0, de-ch;q=0.5", "de-ch, en, fr-ch", "de-CH, en");
        checkFilter("de-ch;q=0.6, *;q=0", "de-ch, fr-ch", "");
        checkFilter("de-ch;q=0.6, de;q=0", "de-ch", "");
        checkFilter("*;q=0.6, en;q=0", "de-ch, hi-in, en", "de-CH, hi-IN");
        checkFilter("en;q=0.1, *-ch;q=0.5, de-ch;q=0", "de-ch, en, fr-ch", "fr-CH, en");
        checkFilter("de-ch;q=0.5, *-ch;q=0", "de-ch, fr-ch", "");
        checkFilter("*-ch;q=0.5, *;q=0", "de-ch, fr-ch", "");
        checkFilter("*;q=0.6, *-Latn;q=0", "de-ch, hi-in, en-Latn", "de-CH, hi-IN");
        if (err) {
            throw new RuntimeException("[LocaleMatcher method(s) failed]");
        }
    }

    private static void checkLookup(String ranges, String tags, String expectedLocale) {
        List<Locale.LanguageRange> priorityList = Locale.LanguageRange.parse(ranges);
        List<Locale> localeList = generateLocales(tags);
        Locale loc = Locale.lookup(priorityList, localeList);
        String actualLocale = loc.toLanguageTag();
        if (!actualLocale.equals(expectedLocale)) {
            System.err.println("Locale.lookup failed with ranges: " + ranges + " Expected: " + expectedLocale + " Actual: " + actualLocale);
            err = true;
        }
    }

    private static void checkFilter(String ranges, String tags, String expectedLocales) {
        List<Locale.LanguageRange> priorityList = Locale.LanguageRange.parse(ranges);
        List<Locale> localeList = generateLocales(tags);
        String actualLocales = getLocalesAsString(Locale.filter(priorityList, localeList));
        if (!actualLocales.equals(expectedLocales)) {
            System.err.println("Locale.filter failed with ranges: " + ranges + " Expected: " + expectedLocales + " Actual: " + actualLocales);
            err = true;
        }
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

    private static String getLocalesAsString(List<Locale> locales) {
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
