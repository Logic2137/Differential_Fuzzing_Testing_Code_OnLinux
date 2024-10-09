



import java.text.*;
import java.time.*;
import java.util.*;

public class Bug8134384 {
    public static void main(String [] args) {
        TimeZone original = TimeZone.getDefault();

        try {
            for (String tz : TimeZone.getAvailableIDs() ) {
                TimeZone.setDefault(TimeZone.getTimeZone(tz));
                
                String date1 = Date.from(Instant.parse("2015-06-21T00:00:00.00Z")).toString();
                testParse(Locale.ENGLISH, date1, tz);
                testParse(Locale.US, date1, tz);
                
                String date2 = Date.from(Instant.parse("2015-12-22T00:00:00.00Z")).toString();
                testParse(Locale.ENGLISH, date2, tz);
                testParse(Locale.US, date2, tz);
            }
        } finally {
            TimeZone.setDefault(original);
        }
    }

    private static void testParse(Locale locale, String date, String tz) {
        try {
            new SimpleDateFormat("EEE MMM d hh:mm:ss z yyyy", locale).parse(date);
            System.out.println(String.format(Locale.ENGLISH, "OK parsing '%s' in locale '%s', tz: %s", date, locale, tz));
        } catch (ParseException pe) {
            throw new RuntimeException(String.format(Locale.ENGLISH, "ERROR parsing '%s' in locale '%s', tz: %s: %s", date, locale, tz, pe.toString()));
        }
    }
}
