import java.util.GregorianCalendar;
import static java.util.Calendar.*;

public class Bug6178071 {

    public static void main(String[] args) {
        GregorianCalendar cal = new GregorianCalendar(2004, JANUARY, 1);
        cal.set(HOUR, 1);
        if (cal.get(HOUR_OF_DAY) != 1 || cal.get(HOUR) != 1 || cal.get(AM_PM) != AM) {
            throw new RuntimeException("Unexpected hour of day: " + cal.getTime());
        }
        GregorianCalendar gc = new GregorianCalendar(2006, 5, 16);
        gc.setLenient(false);
        gc.set(HOUR_OF_DAY, 10);
        gc.get(YEAR);
    }
}
