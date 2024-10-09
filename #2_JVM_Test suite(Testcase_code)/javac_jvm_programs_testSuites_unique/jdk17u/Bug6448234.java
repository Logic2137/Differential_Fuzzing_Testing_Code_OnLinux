import java.util.Calendar;
import java.util.Locale;
import static java.util.Calendar.*;

public class Bug6448234 {

    public static void main(String[] args) {
        Calendar jcal = Calendar.getInstance(new Locale("ja", "JP", "JP"));
        Calendar gcal = Calendar.getInstance(Locale.US);
        for (int i = SUNDAY; i <= SATURDAY; i++) {
            jcal.set(DAY_OF_WEEK, i);
            gcal.set(DAY_OF_WEEK, i);
            String j = jcal.getDisplayName(DAY_OF_WEEK, LONG, Locale.US);
            String g = gcal.getDisplayName(DAY_OF_WEEK, LONG, Locale.US);
            if (!j.equals(g)) {
                throw new RuntimeException("Got " + j + ", expected " + g);
            }
            j = jcal.getDisplayName(DAY_OF_WEEK, SHORT, Locale.US);
            g = gcal.getDisplayName(DAY_OF_WEEK, SHORT, Locale.US);
            if (!j.equals(g)) {
                throw new RuntimeException("Got " + j + ", expected " + g);
            }
        }
    }
}
