

package java.text;



import java.util.Calendar;

public class CalendarBuilderTest {
    public static void testCalendarBuilderToString() {
        CalendarBuilder calendarBuilder = new CalendarBuilder();
        calendarBuilder.set(Calendar.YEAR, 2020);
        calendarBuilder.toString();

    }
}
