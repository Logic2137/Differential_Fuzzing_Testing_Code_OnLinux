



import java.util.Calendar;
import java.util.Locale;

import static java.util.Calendar.WEDNESDAY;


public class CalendarDataProviderTest {

    public static void main(String[] s) {
        new CalendarDataProviderTest().test();
    }

    void test() {
        Locale kids = new Locale("ja", "JP", "kids"); 
        Calendar kcal = Calendar.getInstance(kids);

        
        checkResult("firstDayOfWeek", kcal.getFirstDayOfWeek(), WEDNESDAY);
        checkResult("minimalDaysInFirstWeek", kcal.getMinimalDaysInFirstWeek(), 7);
    }

    private <T> void checkResult(String msg, T got, T expected) {
        if (!expected.equals(got)) {
            String s = String.format("%s: got='%s', expected='%s'", msg, got, expected);
            throw new RuntimeException(s);
        }
    }
}
