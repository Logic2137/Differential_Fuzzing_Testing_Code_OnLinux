import java.text.*;
import java.util.*;
import static java.util.GregorianCalendar.*;

public class Bug7130335 {

    private static final TimeZone MOSCOW = TimeZone.getTimeZone("Europe/Moscow");

    private static final TimeZone LONDON = TimeZone.getTimeZone("Europe/London");

    private static final TimeZone LA = TimeZone.getTimeZone("America/Los_Angeles");

    private static final TimeZone[] ZONES = { MOSCOW, LONDON, LA };

    public static void main(String[] args) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z", Locale.US);
        sdf.setTimeZone(MOSCOW);
        Calendar cal = new GregorianCalendar(MOSCOW, Locale.US);
        cal.clear();
        cal.set(1922, SEPTEMBER, 30);
        test(sdf, cal);
        cal.add(DAY_OF_YEAR, 1);
        test(sdf, cal);
        cal.set(1991, MARCH, 31);
        test(sdf, cal);
        cal.add(DAY_OF_YEAR, 1);
        test(sdf, cal);
        cal.setTimeInMillis(System.currentTimeMillis());
        test(sdf, cal);
        test8000529("yyyy-MM-dd HH:mm:ss.SSS Z (z)");
        test8000529("yyyy-MM-dd HH:mm:ss.SSS Z (zzzz)");
        test8000529("yyyy-MM-dd HH:mm:ss.SSS z (Z)");
        test8000529("yyyy-MM-dd HH:mm:ss.SSS zzzz (Z)");
    }

    private static void test(SimpleDateFormat sdf, Calendar cal) throws Exception {
        Date d = cal.getTime();
        String f = sdf.format(d);
        System.out.println(f);
        Date pd = sdf.parse(f);
        String p = sdf.format(pd);
        if (!d.equals(pd) || !f.equals(p)) {
            throw new RuntimeException("format: " + f + ", parse: " + p);
        }
    }

    private static void test8000529(String fmt) throws Exception {
        for (TimeZone tz : ZONES) {
            SimpleDateFormat sdf = new SimpleDateFormat(fmt, Locale.US);
            sdf.setTimeZone(tz);
            Calendar cal = new GregorianCalendar(tz, Locale.US);
            cal.clear();
            cal.set(2012, JUNE, 22);
            test(sdf, cal);
            cal.set(2012, DECEMBER, 22);
            test(sdf, cal);
            cal.setTimeInMillis(System.currentTimeMillis());
            test(sdf, cal);
        }
    }
}
