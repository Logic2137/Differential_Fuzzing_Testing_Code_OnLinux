import java.util.*;
import static java.util.GregorianCalendar.*;

public class Bug6902861 {

    static int errors = 0;

    public static void main(String[] args) {
        Locale loc = Locale.getDefault();
        try {
            Locale.setDefault(Locale.GERMANY);
            test(2010, JANUARY, 1, +1, 1);
            test(2010, JANUARY, 1, +2, 2);
            test(2010, JANUARY, 1, -1, 52);
            test(2010, JANUARY, 1, -2, 51);
            test(2008, DECEMBER, 31, +1, 1);
            test(2008, DECEMBER, 31, +2, 2);
            test(2008, DECEMBER, 31, -1, 52);
            test(2008, DECEMBER, 31, -2, 51);
            if (errors > 0) {
                throw new RuntimeException("Failed");
            }
        } finally {
            Locale.setDefault(loc);
        }
    }

    static void test(int year, int month, int dayOfMonth, int amount, int expected) {
        Calendar calendar = new GregorianCalendar(year, month, dayOfMonth);
        int week = calendar.get(WEEK_OF_YEAR);
        calendar.roll(WEEK_OF_YEAR, amount);
        int got = calendar.get(WEEK_OF_YEAR);
        int y = calendar.get(YEAR);
        if (got != expected || y != year) {
            String date = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
            System.err.printf("%s: roll %+d: got: %d,%2d; expected: %d,%2d%n", date, amount, y, got, year, expected);
            errors++;
        }
    }
}
