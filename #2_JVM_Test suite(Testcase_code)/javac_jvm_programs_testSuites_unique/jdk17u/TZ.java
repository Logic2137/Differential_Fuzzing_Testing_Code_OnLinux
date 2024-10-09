import java.util.TimeZone;
import java.util.Date;

public class TZ {

    public static void main(String[] args) {
        TimeZone tz = TimeZone.getDefault();
        try {
            testMain();
        } finally {
            TimeZone.setDefault(tz);
        }
    }

    static void testMain() {
        String expectedResult = "Sat Feb 01 00:00:00 PST 1997";
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
        new Date();
        TimeZone.setDefault(TimeZone.getTimeZone("PST"));
        @SuppressWarnings("deprecation")
        Date date = new Date(97, 1, 1);
        if (!date.toString().equals(expectedResult)) {
            throw new RuntimeException("Regression bug id #4108737 - Date fails if default time zone changed");
        }
    }
}
