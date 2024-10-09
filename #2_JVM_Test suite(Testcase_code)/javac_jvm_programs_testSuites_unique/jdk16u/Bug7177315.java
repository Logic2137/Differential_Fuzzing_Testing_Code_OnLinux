



import java.text.*;
import java.util.*;

public class Bug7177315 {
    private static final String EXPECTED = "01/01/2012";
    private static final String[] DATA = {
        "01/01/12",
        "01/01/ 12",
        "01/01/       12",
        "1/1/12",
        "1/1/  12"
    };

    public static void main (String[] args) throws ParseException {
        SimpleDateFormat parseFormat = new SimpleDateFormat("MM/dd/yy", Locale.US);
        Calendar cal = new GregorianCalendar(2012-80, Calendar.JANUARY, 1);
        parseFormat.set2DigitYearStart(cal.getTime());
        SimpleDateFormat fmtFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

        for (String text : DATA) {
            Date date = parseFormat.parse(text);
            String got = fmtFormat.format(date);
            if (!EXPECTED.equals(got)) {
                throw new RuntimeException("got: " + got + ", expected: " + EXPECTED);
            }
        }
    }
}
