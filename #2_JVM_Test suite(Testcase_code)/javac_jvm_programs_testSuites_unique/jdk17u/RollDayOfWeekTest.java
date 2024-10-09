import java.util.*;
import static java.util.Calendar.*;

public class RollDayOfWeekTest {

    public static void main(String[] args) {
        int pastYears = 5, futureYears = 23;
        if (args.length == 2) {
            pastYears = Integer.parseInt(args[0]);
            pastYears = Math.max(1, Math.min(pastYears, 5));
            futureYears = Integer.parseInt(args[1]);
            futureYears = Math.max(1, Math.min(futureYears, 28));
        }
        System.out.printf("Test [%d .. %+d] year range.%n", -pastYears, futureYears);
        Calendar cal = new GregorianCalendar();
        int year = cal.get(YEAR) - pastYears;
        for (int fdw = SUNDAY; fdw <= SATURDAY; fdw++) {
            for (int mdifw = 1; mdifw <= 7; mdifw++) {
                cal.clear();
                cal.setFirstDayOfWeek(fdw);
                cal.setMinimalDaysInFirstWeek(mdifw);
                cal.set(year, JANUARY, 1);
                checkRoll(cal, futureYears);
            }
        }
        year = -1;
        for (int fdw = SUNDAY; fdw <= SATURDAY; fdw++) {
            for (int mdifw = 1; mdifw <= 7; mdifw++) {
                cal.clear();
                cal.setFirstDayOfWeek(fdw);
                cal.setMinimalDaysInFirstWeek(mdifw);
                cal.set(year, JANUARY, 1);
                checkRoll(cal, 4);
            }
        }
    }

    static void checkRoll(Calendar cal, int years) {
        Calendar cal2 = null, cal3 = null, prev = null;
        for (int x = 0; x < (int) (365.2425 * years); x++) {
            cal2 = (Calendar) cal.clone();
            cal3 = (Calendar) cal.clone();
            for (int i = 0; i < 10; i++) {
                prev = (Calendar) cal2.clone();
                cal2.roll(Calendar.DAY_OF_WEEK, +1);
                roll(cal3, +1);
                long t2 = cal2.getTimeInMillis();
                long t3 = cal3.getTimeInMillis();
                if (t2 != t3) {
                    System.err.println("prev: " + prev.getTime() + "\n" + prev);
                    System.err.println("cal2: " + cal2.getTime() + "\n" + cal2);
                    System.err.println("cal3: " + cal3.getTime() + "\n" + cal3);
                    throw new RuntimeException("+1: t2=" + t2 + ", t3=" + t3);
                }
            }
            for (int i = 0; i < 10; i++) {
                prev = (Calendar) cal2.clone();
                cal2.roll(Calendar.DAY_OF_WEEK, -1);
                roll(cal3, -1);
                long t2 = cal2.getTimeInMillis();
                long t3 = cal3.getTimeInMillis();
                if (t2 != t3) {
                    System.err.println("prev: " + prev.getTime() + "\n" + prev);
                    System.err.println("cal2: " + cal2.getTime() + "\n" + cal2);
                    System.err.println("cal3: " + cal3.getTime() + "\n" + cal3);
                    throw new RuntimeException("-1: t2=" + t2 + ", t3=" + t3);
                }
            }
            cal.add(DAY_OF_YEAR, +1);
        }
    }

    static void roll(Calendar cal, int n) {
        int doy = cal.get(DAY_OF_YEAR);
        int diff = cal.get(DAY_OF_WEEK) - cal.getFirstDayOfWeek();
        if (diff < 0) {
            diff += 7;
        }
        int dow1 = doy - diff;
        n %= 7;
        doy += n;
        if (doy < dow1) {
            doy += 7;
        } else if (doy >= dow1 + 7) {
            doy -= 7;
        }
        cal.set(DAY_OF_YEAR, doy);
    }
}
