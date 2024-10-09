import java.text.*;
import java.util.*;
import static java.util.Calendar.*;

public class Bug8075548 {

    static int errors = 0;

    public static void main(String[] args) throws Throwable {
        Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse("2010-09-15");
        String[][] FORMAT_PAIRS = { { "LLLL", "MMMM" }, { "LLL", "MMM" } };
        Locale[] LOCALES = { Locale.ENGLISH, Locale.JAPANESE };
        for (Locale locale : LOCALES) {
            for (String[] formats : FORMAT_PAIRS) {
                String el = new SimpleDateFormat(formats[0], locale).format(date);
                String em = new SimpleDateFormat(formats[1], locale).format(date);
                if (!el.equals(em)) {
                    errors++;
                    System.err.println(locale + ": " + formats[0] + " -> " + el + ", " + formats[1] + " -> " + em);
                }
            }
        }
        for (Locale locale : LOCALES) {
            testDisplayNames(locale, LONG_FORMAT, LONG_STANDALONE);
            testDisplayNames(locale, SHORT_FORMAT, SHORT_STANDALONE);
            testDisplayNames(locale, NARROW_FORMAT, NARROW_STANDALONE);
        }
        if (errors > 0) {
            throw new RuntimeException("Failed");
        }
    }

    private static void testDisplayNames(Locale locale, int formatStyle, int standaloneStyle) {
        Map<String, Integer> map = new HashMap<>();
        for (int month = JANUARY; month <= DECEMBER; month++) {
            Calendar cal = new GregorianCalendar(2015, month, 1);
            String format = cal.getDisplayName(MONTH, formatStyle, locale);
            String standalone = cal.getDisplayName(MONTH, standaloneStyle, locale);
            if (!format.equals(standalone)) {
                System.err.println("Calendar.getDisplayName: " + (month + 1) + ", locale=" + locale + ", format=" + format + ", standalone=" + standalone);
                errors++;
            }
            if (standalone != null) {
                map.put(standalone, month);
            }
        }
        if (formatStyle == NARROW_FORMAT) {
            return;
        }
        Calendar cal = new GregorianCalendar(2015, JANUARY, 1);
        Map<String, Integer> mapStandalone = cal.getDisplayNames(MONTH, standaloneStyle, locale);
        if (!map.equals(mapStandalone)) {
            System.err.printf("Calendar.getDisplayNames: locale=%s%n    map=%s%n    mapStandalone=%s%n", locale, map, mapStandalone);
            errors++;
        }
        Map<String, Integer> mapAll = cal.getDisplayNames(MONTH, ALL_STYLES, locale);
        if (!mapAll.entrySet().containsAll(map.entrySet())) {
            System.err.printf("Calendar.getDisplayNames: locale=%s%n    map=%s%n    mapAll=%s%n", locale, map, mapAll);
            errors++;
        }
    }
}
