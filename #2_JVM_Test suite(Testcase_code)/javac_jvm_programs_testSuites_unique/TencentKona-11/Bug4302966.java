



import java.util.Calendar;
import java.util.Locale;

public class Bug4302966 {

    public static void main(String[] args) {
        Calendar czechCalendar = Calendar.getInstance(new Locale("cs", "CZ"));
        int firstDayOfWeek = czechCalendar.getFirstDayOfWeek();
        if (firstDayOfWeek != Calendar.MONDAY) {
            throw new RuntimeException();
        }
    }
}
