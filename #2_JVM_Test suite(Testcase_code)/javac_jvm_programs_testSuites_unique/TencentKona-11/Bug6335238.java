



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class Bug6335238 {

    static final long UTC_LONG = 974534460000L;
    static final String TIME_STRING = "2000/11/18 00:01:00";
    static SimpleDateFormat masterSdf;
    static int duration = 180;
    static boolean stopped = false;
    static boolean err = false;

    public static void main(String[] args) {
        if (args.length == 1) {
            duration = Math.max(10, Integer.parseInt(args[0]));
        }
        Locale savedLocale = Locale.getDefault();
        TimeZone savedTimeZone = TimeZone.getDefault();

        TimeZone.setDefault(TimeZone.getTimeZone("US/Pacific"));
        Locale.setDefault(Locale.US);

        masterSdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        try {
            
            Date d = masterSdf.parse(TIME_STRING);

            new Bug6335238();
        } catch (Exception e) {
            System.err.println(e);
            err = true;
        } finally {
            TimeZone.setDefault(savedTimeZone);
            Locale.setDefault(savedLocale);

            if (err) {
                throw new RuntimeException("Failed: Multiple DateFormat instances didn't work correctly.");
            } else {
                System.out.println("Passed.");
            }
        }
    }

    public Bug6335238() {
        stopped = false;

        DateParseThread d1 = new DateParseThread();
        DateFormatThread d2 = new DateFormatThread();
        DateParseThread d3 = new DateParseThread();
        DateFormatThread d4 = new DateFormatThread();

        d1.start();
        d2.start();
        d3.start();
        d4.start();

        try {
            Thread.sleep(duration * 1000);
        }
        catch (Exception e) {
            System.err.println(e);
            err = true;
        }

        stopped = true;
    }

    class DateFormatThread extends Thread {

        public void run() {
            int i = 0;

            while (!stopped) {
                SimpleDateFormat sdf;
                synchronized (masterSdf) {
                    sdf = (SimpleDateFormat)masterSdf.clone();
                }

                i++;
                String s = sdf.format(new Date(UTC_LONG));

                if (!s.equals(TIME_STRING)) {
                    stopped = true;
                    err = true;

                    throw new RuntimeException("Formatting Date Error: counter=" +
                        i + ", Got<" + s + "> != Expected<" + TIME_STRING + ">");
                }
            }
        }
    }

    class DateParseThread extends Thread {

        public void run() {
            int i = 0;

            while (!stopped) {
                SimpleDateFormat sdf;
                synchronized (masterSdf) {
                    sdf = (SimpleDateFormat)masterSdf.clone();
                }

                i++;
                Date date;
                try {
                    date = sdf.parse(TIME_STRING);
                    long t = date.getTime();

                    if (t != UTC_LONG) {
                        stopped = true;
                        err = true;

                        throw new RuntimeException("Parsing Date Error: counter=" +
                            i + " Got:" + t + "<" + sdf.format(date) +
                            "> != " + UTC_LONG);
                    }
                }
                catch (ParseException e) {
                    stopped = true;
                    err = true;

                    throw new RuntimeException(e);
                }
            }
        }
    }
}
