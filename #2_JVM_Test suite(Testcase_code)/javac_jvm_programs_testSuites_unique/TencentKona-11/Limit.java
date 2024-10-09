

import java.util.*;
import java.text.*;


public class Limit {
    static final long ONE_DAY = 24*60*60*1000L;

    public static void main(String args[]) throws Exception {
        GregorianCalendar c = new GregorianCalendar();
        DateFormat fmt = new SimpleDateFormat("EEEE, MMMM dd, yyyy G", Locale.US);
        long bigMillis = 300000000000000L;

        try {
            
            
            
            
            c.setTime(new Date(-bigMillis));
            String s = fmt.format(c.getTime());
            Date d = fmt.parse(s);
            if (Math.abs(d.getTime() + bigMillis) >= ONE_DAY) {
                throw new Exception(s + " != " + fmt.format(d));
            }

            c.setTime(new Date(+bigMillis));
            s = fmt.format(c.getTime());
            d = fmt.parse(s);
            if (Math.abs(d.getTime() - bigMillis) >= ONE_DAY) {
                throw new Exception(s + " != " + fmt.format(d));
            }
        } catch (IllegalArgumentException | ParseException e) {
            throw e;
        }
    }
}
