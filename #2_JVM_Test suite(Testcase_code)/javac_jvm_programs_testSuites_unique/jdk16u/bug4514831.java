



import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import static java.util.GregorianCalendar.*;


public class bug4514831 {

    public static void main(String[] args) {
        Locale savedLocale = Locale.getDefault();
        TimeZone savedTimeZone = TimeZone.getDefault();
        boolean err = false;

        String golden_data1 = "27-28 28-29 29-30 30-31 31-1 1-2 2-3 ";
        String golden_data2 = "27-28 28-29 29-30 30-31 31-25 25-26 26-27 ";
        String golden_data3 = "1-8 8-15 15-22 22-29 29-1 1-8 8-15 ";

        try {
            Locale.setDefault(Locale.US);
            TimeZone.setDefault(TimeZone.getTimeZone("US/Pacific"));

            String test_roll = "";
            GregorianCalendar c_roll = new GregorianCalendar(2001, OCTOBER, 27);
            for (int i = 0; i < 7; i++) {
                test_roll += c_roll.get(DAY_OF_MONTH) + "-";
                c_roll.roll(DAY_OF_YEAR, true);
                test_roll += c_roll.get(DAY_OF_MONTH) + " ";
            }
            if (!test_roll.equals(golden_data1)) {
                err = true;
                System.err.println("Wrong roll(DAY_OF_YEAR) transition: got "
                        + test_roll + "expected " + golden_data1);
            }

            test_roll = "";
            c_roll = new GregorianCalendar(2001, OCTOBER, 27);
            c_roll.setFirstDayOfWeek(THURSDAY);
            for (int i = 0; i < 7; i++) {
                test_roll += c_roll.get(DAY_OF_MONTH) + "-";
                c_roll.roll(DAY_OF_WEEK, true);
                test_roll += c_roll.get(DAY_OF_MONTH) + " ";
            }
            if (!test_roll.equals(golden_data2)) {
                err = true;
                System.err.println("Wrong roll(DAY_OF_WEEK) transition: got "
                        + test_roll + "expected " + golden_data2);
            }

            test_roll = "";
            c_roll = new GregorianCalendar(2001, OCTOBER, 1);
            for (int i = 0; i < 7; i++) {
                test_roll += c_roll.get(DAY_OF_MONTH) + "-";
                c_roll.roll(DAY_OF_WEEK_IN_MONTH, true);
                test_roll += c_roll.get(DAY_OF_MONTH) + " ";
            }
            if (!test_roll.equals(golden_data3)) {
                err = true;
                System.err.println("Wrong roll(DAY_OF_WEEK_IN_MONTH) transition: got "
                        + test_roll + "expected " + golden_data3);
            }
        } finally {
            Locale.setDefault(savedLocale);
            TimeZone.setDefault(savedTimeZone);
        }

        if (err) {
            throw new RuntimeException("Wrong roll() transition");
        }
    }
}
