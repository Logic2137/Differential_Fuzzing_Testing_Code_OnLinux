


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class Bug8139572 {

    private static final Locale RUSSIAN = new Locale("ru");
    private static final Date SEPT12 = new GregorianCalendar(2015, Calendar.SEPTEMBER, 12).getTime();

    private static final String[] PATTERNS = {
        "L",
        "dd L",
        "dd L yy",
        "dd L yyyy",
        "LL",
        "dd LL",
        "dd LL yy",
        "dd LL yyyy",
        "LLL",
        "dd LLL",
        "dd LLL yy",
        "dd LLL yyyy",
        "LLLL",
        "dd LLLL",
        "dd LLLL yy",
        "dd LLLL yyyy"
    };

    private static final String[] APPLIED = {
        "9",
        "12 09",
        "12 09 15",
        "12 09 2015",
        "09",
        "12 09",
        "12 09 15",
        "12 09 2015",
        "сентября",
        "12 сентября",
        "12 сентября 15",
        "12 сентября 2015",
        "сентября",
        "12 сентября",
        "12 сентября 15",
        "12 сентября 2015"
    };

    private static final String[] EXPECTED = {
        "9",
        "12 9",
        "12 9 15",
        "12 9 2015",
        "09",
        "12 09",
        "12 09 15",
        "12 09 2015",
        "сент.",
        "12 сент.",
        "12 сент. 15",
        "12 сент. 2015",
        "сентябрь",
        "12 сентябрь",
        "12 сентябрь 15",
        "12 сентябрь 2015"
    };

    public static void main(String[] args) throws ParseException {

        for (int i = 0; i < PATTERNS.length; i++) {
            SimpleDateFormat fmt = new SimpleDateFormat(PATTERNS[i], RUSSIAN);
            Date standAloneDate = fmt.parse(APPLIED[i]);
            String str = fmt.format(standAloneDate);
            if (!EXPECTED[i].equals(str)) {
                throw new RuntimeException("bad result: got '" + str + "', expected '" + EXPECTED[i] + "'");
            }
        }

        SimpleDateFormat fmt = new SimpleDateFormat("", RUSSIAN);
        for (int j = 0; j < PATTERNS.length; j++) {
            fmt.applyPattern(PATTERNS[j]);
            String str = fmt.format(SEPT12);
            if (!EXPECTED[j].equals(str)) {
                throw new RuntimeException("bad result: got '" + str + "', expected '" + EXPECTED[j] + "'");
            }
        }
    }
}
