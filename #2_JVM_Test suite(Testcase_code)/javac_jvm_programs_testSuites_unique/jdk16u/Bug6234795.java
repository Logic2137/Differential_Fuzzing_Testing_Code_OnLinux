



import java.util.GregorianCalendar;
import static java.util.Calendar.AM;
import static java.util.Calendar.AM_PM;
import static java.util.Calendar.HOUR;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.SEPTEMBER;
import java.util.Locale;
import java.util.TimeZone;

public class Bug6234795 {
    public static void main(String[] args) {
        testRoll(HOUR);
        testRoll(HOUR_OF_DAY);
    }

    static void testRoll(int field) {
        GregorianCalendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT"), Locale.US);
        cal.clear();
        cal.set(2005, SEPTEMBER, 12);

        int otherField = (field == HOUR) ? HOUR_OF_DAY : HOUR;
        int unit = (field == HOUR) ? 12 : 24;
        int h;
        for (h = 0; h <= 72; h++) {
            int hour = cal.get(otherField);
            int expected = h % 12;
            if (hour != expected) {
                throw new RuntimeException((field == HOUR ? "HOUR" : "HOUR_OF_DAY")
                                           + "+: h=" + h + ", got " + hour
                                           + ", expected " + expected);
            }
            if (field == HOUR_OF_DAY) {
                int ampm = cal.get(AM_PM);
                expected = (h % unit) / 12;
                if (ampm != expected) {
                    throw new RuntimeException((field == HOUR ? "HOUR" : "HOUR_OF_DAY")
                                               + "+: h=" + h + ", got "
                                               + toString(ampm)
                                               + ", expected " + toString(expected));
                }
            }
            cal.roll(field, +1);
        }
        for (; h >= 0; h--) {
            int hour = cal.get(otherField);
            int expected = h % 12;
            if (hour != expected) {
                throw new RuntimeException((field == HOUR ? "HOUR" : "HOUR_OF_DAY")
                                           + "-: h=" + h + ", got " + hour
                                           + ", expected " + expected);
            }
            if (field == HOUR_OF_DAY) {
                int ampm = cal.get(AM_PM);
                expected = (h % unit) / 12;
                if (ampm != expected) {
                    throw new RuntimeException((field == HOUR ? "HOUR" : "HOUR_OF_DAY")
                                               + "-: h=" + h + ", got " + toString(ampm)
                                               + ", expected " + toString(expected));
                }
            }
            cal.roll(field, -1);
        }
    }

    static String toString(int ampm) {
        return ampm == AM ? "AM" : "PM";
    }
}
