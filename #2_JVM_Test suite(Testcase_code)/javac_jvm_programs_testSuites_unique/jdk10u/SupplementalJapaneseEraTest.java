

import java.text.SimpleDateFormat;
import java.time.chrono.JapaneseChronology;
import java.time.chrono.JapaneseDate;
import java.time.chrono.JapaneseEra;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import static java.util.GregorianCalendar.*;
import java.util.Locale;
import java.util.TimeZone;



public class SupplementalJapaneseEraTest {
    private static final Locale WAREKI_LOCALE = Locale.forLanguageTag("ja-JP-u-ca-japanese");
    private static final String NEW_ERA_NAME = "NewEra";
    private static final String NEW_ERA_ABBR = "N.E.";
    private static int errors = 0;

    public static void main(String[] args) {
        
        switch (args[0]) {
        case "-s":
            
            Calendar cal = new Calendar.Builder()
                .setCalendarType("japanese")
                .setTimeZone(TimeZone.getTimeZone("GMT"))
                .setDate(200, FEBRUARY, 11)
                .build();
            System.out.println(cal.getTimeInMillis());
            break;

        case "-e":
            
            Calendar jcal = new Calendar.Builder()
                .setCalendarType("japanese")
                .setFields(YEAR, 1, DAY_OF_YEAR, 1)
                .build();
            System.out.println(jcal.getDisplayName(ERA, LONG, Locale.US));
            break;

        case "-t":
            
            testProperty();
            break;

        case "-b":
            
            
            testValidation(args[1].replace("\r", "")); 
            break;
        }
        if (errors != 0) {
            throw new RuntimeException("test failed");
        }
    }

    private static void testProperty() {
        Calendar jcal = new Calendar.Builder()
            .setCalendarType("japanese")
            .setFields(YEAR, 1, DAY_OF_YEAR, 1)
            .build();
        Date firstDayOfEra = jcal.getTime();

        jcal.set(ERA, jcal.get(ERA) - 1); 
        jcal.set(YEAR, 1);
        jcal.set(DAY_OF_YEAR, 1);
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(jcal.getTimeInMillis());
        cal.add(YEAR, 199);
        int year = cal.get(YEAR);

        SimpleDateFormat sdf;
        String expected, got;

        
        sdf = new SimpleDateFormat("GGGG y-MM-dd", WAREKI_LOCALE);
        got = sdf.format(firstDayOfEra);
        expected = NEW_ERA_NAME + " 1-02-11";
        if (!expected.equals(got)) {
            System.err.printf("GGGG y-MM-dd: got=\"%s\", expected=\"%s\"%n", got, expected);
            errors++;
        }

        
        sdf = new SimpleDateFormat("G y-MM-dd", WAREKI_LOCALE);
        got = sdf.format(firstDayOfEra);
        expected = NEW_ERA_ABBR+" 1-02-11";
        if (!expected.equals(got)) {
            System.err.printf("G y-MM-dd: got=\"%s\", expected=\"%s\"%n", got, expected);
            errors++;
        }

        
        sdf = new SimpleDateFormat("y", Locale.US);
        int y = Integer.parseInt(sdf.format(firstDayOfEra));
        if (y != year) {
            System.err.printf("Gregorian year: got=%d, expected=%d%n", y, year);
            errors++;
        }

        
        JapaneseDate jdate = JapaneseDate.of(year, 2, 11);
        got = jdate.toString();
        expected = "Japanese " + NEW_ERA_NAME + " 1-02-11";
        if (!expected.equals(got)) {
            System.err.printf("JapaneseDate: got=\"%s\", expected=\"%s\"%n", got, expected);
            errors++;
        }
        JapaneseEra jera = jdate.getEra();
        got = jera.getDisplayName(TextStyle.FULL, Locale.US);
        if (!NEW_ERA_NAME.equals(got)) {
            System.err.printf("JapaneseEra (FULL): got=\"%s\", expected=\"%s\"%n", got, NEW_ERA_NAME);
            errors++;
        }
        got = jera.getDisplayName(TextStyle.SHORT, Locale.US);
        if (!NEW_ERA_NAME.equals(got)) {
            System.err.printf("JapaneseEra (SHORT): got=\"%s\", expected=\"%s\"%n", got, NEW_ERA_NAME);
            errors++;
        }
        got = jera.getDisplayName(TextStyle.NARROW, Locale.US);
        if (!NEW_ERA_ABBR.equals(got)) {
            System.err.printf("JapaneseEra (NARROW): got=\"%s\", expected=\"%s\"%n", got, NEW_ERA_ABBR);
            errors++;
        }
        got = jera.getDisplayName(TextStyle.NARROW_STANDALONE, Locale.US);
        if (!NEW_ERA_ABBR.equals(got)) {
            System.err.printf("JapaneseEra (NARROW_STANDALONE): got=\"%s\", expected=\"%s\"%n", got, NEW_ERA_ABBR);
            errors++;
        }

        
        got = DateTimeFormatter
            .ofPattern("GGGG G GGGGG")
            .withLocale(Locale.US)
            .withChronology(JapaneseChronology.INSTANCE)
            .format(jdate);
        expected = NEW_ERA_NAME + " " + NEW_ERA_NAME + " " + NEW_ERA_ABBR;
        if (!expected.equals(got)) {
            System.err.printf("java.time formatter full/short/narrow names: got=\"%s\", expected=\"%s\"%n", got, expected);
            errors++;
        }
    }

    private static void testValidation(String eraName) {
        Calendar jcal = new Calendar.Builder()
            .setCalendarType("japanese")
            .setFields(YEAR, 1, DAY_OF_YEAR, 1)
            .build();
        if (!jcal.getDisplayName(ERA, LONG, Locale.US).equals(eraName)) {
            errors++;
            String prop = System.getProperty("jdk.calendar.japanese.supplemental.era");
            System.err.println("Era changed with invalid property: " + prop);
        }
    }
}
