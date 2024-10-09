import java.util.GregorianCalendar;
import static java.util.Calendar.*;

public class bug4028518 {

    public static void main(String[] args) {
        GregorianCalendar cal1 = new GregorianCalendar();
        GregorianCalendar cal2 = (GregorianCalendar) cal1.clone();
        printdate(cal1, "cal1: ");
        printdate(cal2, "cal2 - cloned(): ");
        cal1.add(DAY_OF_MONTH, 1);
        printdate(cal1, "cal1 after adding 1 day: ");
        printdate(cal2, "cal2 should be unmodified: ");
        if (cal1.get(DAY_OF_MONTH) == cal2.get(DAY_OF_MONTH)) {
            throw new RuntimeException("cloned GregorianCalendar modified");
        }
    }

    private static void printdate(GregorianCalendar cal, String string) {
        System.out.println(string + (cal.get(MONTH) + 1) + "/" + cal.get(DAY_OF_MONTH) + "/" + cal.get(YEAR));
    }
}
