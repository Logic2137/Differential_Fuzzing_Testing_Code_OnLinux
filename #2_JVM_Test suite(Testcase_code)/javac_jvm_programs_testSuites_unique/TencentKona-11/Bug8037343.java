



import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Bug8037343
{

    public static void main(String[] arg)
    {
        final Locale esDO = new Locale("es", "DO");
        final String expectedShort = "31/03/12";
        final String expectedMedium = "31/03/2012";

        int errors = 0;
        DateFormat format;
        String result;

        Calendar cal = Calendar.getInstance(esDO);
        cal.set(Calendar.DAY_OF_MONTH, 31);
        cal.set(Calendar.MONTH, Calendar.MARCH);
        cal.set(Calendar.YEAR, 2012);

        format = DateFormat.getDateInstance(DateFormat.SHORT, esDO);
        result = format.format(cal.getTime());
        if (!expectedShort.equals(result)) {
            System.out.println(String.format("Short Date format for es_DO is not as expected. Expected: [%s] Actual: [%s]", expectedShort, result));
            errors++;
        }

        format = DateFormat.getDateInstance(DateFormat.MEDIUM, esDO);
        result = format.format(cal.getTime());
        if (!expectedMedium.equals(result)) {
            System.out.println(String.format("Medium Date format for es_DO is not as expected. Expected: [%s] Actual: [%s]", expectedMedium, result));
            errors++;
        }

        if (errors > 0) {
            throw new RuntimeException();
        }
    }

}
