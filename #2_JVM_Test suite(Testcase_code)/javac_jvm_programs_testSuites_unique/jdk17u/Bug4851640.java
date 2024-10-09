import java.util.GregorianCalendar;
import static java.util.Calendar.*;

public class Bug4851640 {

    public static void main(String[] args) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.clear();
        cal.set(YEAR, 2003);
        long t = cal.getTime().getTime();
        if (cal.isSet(MONTH) || cal.isSet(DAY_OF_MONTH)) {
            throw new RuntimeException("After getTime(): MONTH field=" + cal.isSet(MONTH) + ", DAY_OF_MONTH field=" + cal.isSet(DAY_OF_MONTH));
        }
        int y = cal.get(YEAR);
        if (!(cal.isSet(MONTH) && cal.isSet(DAY_OF_MONTH))) {
            throw new RuntimeException("After get(): MONTH field=" + cal.isSet(MONTH) + ", DAY_OF_MONTH field=" + cal.isSet(DAY_OF_MONTH));
        }
    }
}
