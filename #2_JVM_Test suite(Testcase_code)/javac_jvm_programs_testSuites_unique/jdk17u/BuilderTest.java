import java.time.LocalDateTime;
import java.util.*;
import static java.util.Calendar.*;

public class BuilderTest {

    private static final Locale jaJPJP = new Locale("ja", "JP", "JP");

    private static final Locale thTH = new Locale("th", "TH");

    private static final TimeZone LA = TimeZone.getTimeZone("America/Los_Angeles");

    private static final TimeZone TOKYO = TimeZone.getTimeZone("Asia/Tokyo");

    private static int error;

    public static void main(String[] args) {
        TimeZone tz = TimeZone.getDefault();
        Locale loc = Locale.getDefault();
        try {
            TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
            Locale.setDefault(Locale.US);
            Calendar.Builder calb;
            Calendar cal, expected;
            calb = builder();
            long time = System.currentTimeMillis();
            cal = calb.setInstant(time).build();
            expected = new GregorianCalendar();
            expected.setTimeInMillis(time);
            check(cal, expected);
            calb = builder();
            cal = calb.setInstant(new Date(time)).build();
            check(cal, expected);
            calb = builder();
            cal = calb.setTimeZone(LA).setInstant(time).build();
            expected = new GregorianCalendar(LA, Locale.US);
            expected.setTimeInMillis(time);
            check(cal, expected);
            calb = builder();
            cal = calb.setTimeZone(TOKYO).setInstant(time).build();
            expected = new GregorianCalendar(TOKYO, Locale.US);
            expected.setTimeInMillis(time);
            check(cal, expected);
            calb = builder();
            cal = calb.set(YEAR, 2013).set(MONTH, JANUARY).set(DAY_OF_MONTH, 31).set(HOUR_OF_DAY, 10).set(MINUTE, 20).set(SECOND, 30).set(MILLISECOND, 40).build();
            expected = new GregorianCalendar(2013, JANUARY, 31, 10, 20, 30);
            expected.set(MILLISECOND, 40);
            check(cal, expected);
            calb = builder();
            cal = calb.setFields(YEAR, 2013, MONTH, JANUARY, DAY_OF_MONTH, 31, HOUR_OF_DAY, 10, MINUTE, 20, SECOND, 30, MILLISECOND, 40).build();
            check(cal, expected);
            calb = builder();
            cal = calb.setFields(YEAR, 2013, MONTH, DECEMBER, DAY_OF_MONTH, 31, HOUR_OF_DAY, 10, MINUTE, 20, SECOND, 30, MILLISECOND, 40).set(DAY_OF_YEAR, 31).build();
            check(cal, expected);
            calb = builder();
            cal = calb.setDate(2013, JANUARY, 31).setTimeOfDay(10, 20, 30, 40).build();
            check(cal, expected);
            calb = builder().setCalendarType("iso8601");
            cal = calb.setWeekDate(2013, 1, MONDAY).setTimeOfDay(10, 20, 30).build();
            expected = getISO8601();
            expected.set(2012, DECEMBER, 31, 10, 20, 30);
            check(cal, expected);
            cal = builder().setFields(MONTH, JANUARY, DAY_OF_MONTH, 9).build();
            check(cal, new GregorianCalendar(1970, JANUARY, 9));
            calb = builder();
            cal = calb.build();
            expected = new GregorianCalendar();
            expected.clear();
            check(cal, expected);
            calb = builder();
            cal = calb.setCalendarType("buddhist").setDate(2556, JANUARY, 31).build();
            expected = Calendar.getInstance(thTH);
            expected.clear();
            expected.set(2556, JANUARY, 31);
            check(cal, expected);
            calb = builder();
            cal = calb.setLocale(thTH).setDate(2556, JANUARY, 31).build();
            check(cal, expected);
            cal = builder().setCalendarType("japanese").setFields(YEAR, 1, DAY_OF_YEAR, 1).build();
            expected = Calendar.getInstance(jaJPJP);
            expected.clear();
            if (LocalDateTime.now().isBefore(LocalDateTime.of(2019, 5, 1, 0, 0))) {
                expected.set(1, JANUARY, 8);
            } else {
                expected.set(1, MAY, 1);
            }
            check(cal, expected);
            calb = builder();
            cal = calb.setLocale(jaJPJP).setFields(YEAR, 1, DAY_OF_YEAR, 1).build();
            check(cal, expected);
            testExceptions();
        } finally {
            Locale.setDefault(loc);
            TimeZone.setDefault(tz);
        }
        if (error > 0) {
            throw new RuntimeException("Failed");
        }
    }

    private static void testExceptions() {
        Calendar.Builder calb;
        Calendar cal;
        try {
            calb = builder().setInstant((Date) null);
            noException("setInstant((Date)null)");
        } catch (NullPointerException npe) {
        }
        try {
            calb = builder().setCalendarType(null);
            noException("setCalendarType(null)");
        } catch (NullPointerException npe) {
        }
        try {
            calb = builder().setLocale(null);
            noException("setLocale(null)");
        } catch (NullPointerException npe) {
        }
        try {
            calb = builder().setTimeZone(null);
            noException("setTimeZone(null)");
        } catch (NullPointerException npe) {
        }
        try {
            calb = builder().set(100, 2013);
            noException("set(100, 2013)");
        } catch (IllegalArgumentException e) {
        }
        try {
            calb = builder().setFields(100, 2013);
            noException("setFields(100, 2013)");
        } catch (IllegalArgumentException e) {
        }
        try {
            calb = builder().setFields(YEAR, 2013, MONTH);
            noException("setFields(YEAR, 2013, MONTH)");
        } catch (IllegalArgumentException e) {
        }
        try {
            calb = builder().setCalendarType("foo");
            noException("setCalendarType(\"foo\")");
        } catch (IllegalArgumentException e) {
        }
        try {
            calb = builder().setWeekDefinition(8, 1);
            noException("setWeekDefinition(8, 1)");
        } catch (IllegalArgumentException e) {
        }
        try {
            calb = builder().setWeekDefinition(SUNDAY, 0);
            noException("setWeekDefinition(8, 1)");
        } catch (IllegalArgumentException e) {
        }
        try {
            calb = builder().setInstant(new Date()).setDate(2013, JANUARY, 1);
            noException("setInstant(new Date()).setDate(2013, JANUARY, 1)");
        } catch (IllegalStateException e) {
        }
        try {
            calb = builder().setDate(2013, JANUARY, 1).setInstant(new Date());
            noException("setDate(2013, JANUARY, 1).setInstant(new Date())");
        } catch (IllegalStateException e) {
        }
        try {
            calb = builder().setCalendarType("iso8601").setCalendarType("japanese");
            noException("setCalendarType(\"iso8601\").setCalendarType(\"japanese\")");
        } catch (IllegalStateException e) {
        }
        calb = nonLenientBuilder().set(MONTH, 100);
        checkException(calb, IllegalArgumentException.class);
        calb = nonLenientBuilder().setTimeOfDay(23, 59, 70);
        checkException(calb, IllegalArgumentException.class);
        calb = builder().setCalendarType("japanese").setWeekDate(2013, 1, MONDAY);
        checkException(calb, IllegalArgumentException.class);
    }

    private static Calendar.Builder builder() {
        return new Calendar.Builder();
    }

    private static Calendar.Builder nonLenientBuilder() {
        return builder().setLenient(false);
    }

    private static Calendar getISO8601() {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setFirstDayOfWeek(MONDAY);
        cal.setMinimalDaysInFirstWeek(4);
        cal.setGregorianChange(new Date(Long.MIN_VALUE));
        cal.clear();
        return cal;
    }

    private static void check(Calendar cal, Calendar expected) {
        if (!cal.equals(expected)) {
            error++;
            System.err.println("FAILED:");
            System.err.println("\t     cal = " + cal.getTime());
            System.err.println("\texpected = " + expected.getTime());
            System.err.printf("\tcal = %s%n\texp = %s%n", cal, expected);
        }
    }

    private static void checkException(Calendar.Builder calb, Class<? extends Exception> exception) {
        try {
            Calendar cal = calb.build();
            error++;
            System.err.println("expected exception: " + exception);
        } catch (Exception e) {
            if (!e.getClass().equals(exception)) {
                error++;
                System.err.println("unexpected exception: " + e.getClass() + ", expected: " + exception);
            }
        }
    }

    private static void noException(String msg) {
        error++;
        System.err.println("no exception with " + msg);
    }
}
