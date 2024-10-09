


import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Bug8081794 {

    public static void main(String[] args) {
        String date = "13 Jan 2005 21:45:34 ABC";
        String format = "dd MMM yyyy HH:mm:ss z";
        ParsePosition pp = new ParsePosition(0);
        pp.setIndex(0);
        SimpleDateFormat sd = new SimpleDateFormat(format, Locale.ENGLISH);
        Date d = sd.parse(date, pp);
        int errorIndex = pp.getErrorIndex();
        if (errorIndex == 21) {
            System.out.println(": passed");
        } else {
            System.out.println(": failed");
            throw new RuntimeException("Failed with wrong index: " + errorIndex);
        }
    }
}
