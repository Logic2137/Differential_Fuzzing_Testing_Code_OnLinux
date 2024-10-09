



import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import static java.util.GregorianCalendar.*;

public class NarrowNamesTest {
    private static final Locale US = Locale.US;
    private static final Locale JAJPJP = new Locale("ja", "JP", "JP");
    private static final Locale THTH = new Locale("th", "TH");

    private static final String RESET_INDEX = "RESET_INDEX";

    private static int errors = 0;

    private static String providers;

    
    public static void main(String[] args) {
        providers = args[0];

        test(US, ERA, "B",
             ERA, BC, YEAR, 1);
        test(US, ERA, "A",
             ERA, AD, YEAR, 2012);
        test(US, DAY_OF_WEEK, "S",
             YEAR, 2012, MONTH, DECEMBER, DAY_OF_MONTH, 23);
        test(US, AM_PM, "a",
             HOUR_OF_DAY, 10);
        test(US, AM_PM, "p",
             HOUR_OF_DAY, 23);
        test(JAJPJP, DAY_OF_WEEK,
             LocalDateTime.now().isBefore(LocalDateTime.of(2019, 5, 1, 0, 0)) ?
                "\u65e5" : "\u706b", 
             YEAR, 24, MONTH, DECEMBER, DAY_OF_MONTH, 23);
        test(THTH, MONTH, NARROW_STANDALONE, "\u0e18.\u0e04.",
             YEAR, 2555, MONTH, DECEMBER, DAY_OF_MONTH, 5);
        test(THTH, DAY_OF_WEEK, "\u0e1e",
             YEAR, 2555, MONTH, DECEMBER, DAY_OF_MONTH, 5);

        testMap(US, DAY_OF_WEEK, ALL_STYLES, 
                "", 
                "Sunday",    
                "Monday",    
                "Tuesday",   
                "Wednesday", 
                "Thursday",  
                "Friday",    
                "Saturday",  
                RESET_INDEX,
                "", 
                "Sun",       
                "Mon",       
                "Tue",       
                "Wed",       
                "Thu",       
                "Fri",       
                "Sat"        
                );
        testMap(US, DAY_OF_WEEK, NARROW_FORMAT); 
        testMap(US, AM_PM, ALL_STYLES,
                "AM", "PM",
                RESET_INDEX,
                "a", "p");
        testMap(JAJPJP, DAY_OF_WEEK, NARROW_STANDALONE,
                "", 
                "\u65e5",
                "\u6708",
                "\u706b",
                "\u6c34",
                "\u6728",
                "\u91d1",
                "\u571f");
        testMap(JAJPJP, DAY_OF_WEEK, NARROW_FORMAT,
                "", 
                "\u65e5",
                "\u6708",
                "\u706b",
                "\u6c34",
                "\u6728",
                "\u91d1",
                "\u571f");
        testMap(THTH, MONTH, NARROW_FORMAT,
                "\u0e21.\u0e04.",
                "\u0e01.\u0e1e.",
                "\u0e21\u0e35.\u0e04.",
                "\u0e40\u0e21.\u0e22.",
                "\u0e1e.\u0e04.",
                (providers.startsWith("CLDR") ?
                    "\u0e21\u0e34.\u0e22." :
                    "\u0e21\u0e34.\u0e22"),  
                "\u0e01.\u0e04.",
                "\u0e2a.\u0e04.",
                "\u0e01.\u0e22.",
                "\u0e15.\u0e04.",
                "\u0e1e.\u0e22.",
                "\u0e18.\u0e04.");
        testMap(THTH, MONTH, NARROW_STANDALONE,
                "\u0e21.\u0e04.",
                "\u0e01.\u0e1e.",
                "\u0e21\u0e35.\u0e04.",
                "\u0e40\u0e21.\u0e22.",
                "\u0e1e.\u0e04.",
                "\u0e21\u0e34.\u0e22.",
                "\u0e01.\u0e04.",
                "\u0e2a.\u0e04.",
                "\u0e01.\u0e22.",
                "\u0e15.\u0e04.",
                "\u0e1e.\u0e22.",
                "\u0e18.\u0e04.");

        if (errors != 0) {
            throw new RuntimeException("test failed");
        }
    }

    private static void test(Locale locale, int field, String expected, int... data) {
        test(locale, field, NARROW_FORMAT, expected, data);
    }

    private static void test(Locale locale, int field, int style, String expected, int... fieldValuePairs) {
        Calendar cal = Calendar.getInstance(locale);
        cal.clear();
        for (int i = 0; i < fieldValuePairs.length;) {
            int f = fieldValuePairs[i++];
            int v = fieldValuePairs[i++];
            cal.set(f, v);
        }
        String got = cal.getDisplayName(field, style, locale);
        if (!expected.equals(got)) {
            System.err.printf("test: locale=%s, field=%d, value=%d, style=%d, got=\"%s\", expected=\"%s\"%n",
                              locale, field, cal.get(field), style, got, expected);
            errors++;
        }
    }

    private static void testMap(Locale locale, int field, int style, String... expected) {
        Map<String, Integer> expectedMap = null;
        if (expected.length > 0) {
            expectedMap = new TreeMap<>(LengthBasedComparator.INSTANCE);
            int index = 0;
            for (int i = 0; i < expected.length; i++) {
                if (expected[i].isEmpty()) {
                    index++;
                    continue;
                }
                if (expected[i] == RESET_INDEX) {
                    index = 0;
                    continue;
                }
                expectedMap.put(expected[i], index++);
            }
        }
        Calendar cal = Calendar.getInstance(locale);
        Map<String, Integer> got = cal.getDisplayNames(field, style, locale);
        if (!(expectedMap == null && got == null)
            && !(expectedMap != null && expectedMap.equals(got))) {
            System.err.printf("testMap: locale=%s, field=%d, style=%d, expected=%s, got=%s%n",
                              locale, field, style, expectedMap, got);
            errors++;
        }
    }

    
    private static class LengthBasedComparator implements Comparator<String> {
        private static final LengthBasedComparator INSTANCE = new LengthBasedComparator();

        private LengthBasedComparator() {
        }

        @Override
        public int compare(String o1, String o2) {
            int n = o2.length() - o1.length();
            return (n == 0) ? o1.compareTo(o2) : n;
        }
    }
}
