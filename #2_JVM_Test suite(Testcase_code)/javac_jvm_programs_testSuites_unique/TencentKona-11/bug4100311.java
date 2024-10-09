



import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Date;

public class bug4100311
{
    @SuppressWarnings("deprecation")
    public static void main(String args[])
    {
        GregorianCalendar cal = new GregorianCalendar();
        cal.set(Calendar.YEAR, 1997);
        cal.set(Calendar.DAY_OF_YEAR, 1);
        Date d = cal.getTime();             
        if (d.getMonth() != 0 || d.getDate() != 1) {
            throw new RuntimeException("Date isn't Jan 1");
        }
    }
}
