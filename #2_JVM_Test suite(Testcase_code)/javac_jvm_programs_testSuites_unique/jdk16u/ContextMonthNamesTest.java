



import java.text.*;
import java.util.*;

public class ContextMonthNamesTest {
    static Locale CZECH = new Locale("cs");
    static Date JAN30 = new GregorianCalendar(2012, Calendar.JANUARY, 30).getTime();

    static String[] PATTERNS = {
        "d. MMMM yyyy", 
        "d. MMM yyyy",  
        "MMMM",         
        "MMM",          
        "d. LLLL yyyy", 
        "d. LLL yyyy",  
    };
    
    static String[] EXPECTED = {
        "30. ledna 2012",
        "30. Led 2012",
        "leden",
        "I",
        "30. leden 2012",
        "30. I 2012",
    };

    public static void main(String[] args) {
        SimpleDateFormat fmt = new SimpleDateFormat("", CZECH);
        for (int i = 0; i < PATTERNS.length; i++) {
            fmt.applyPattern(PATTERNS[i]);
            String str = fmt.format(JAN30);
            if (!EXPECTED[i].equals(str)) {
                throw new RuntimeException("bad result: got '" + str
                                           + "', expected '" + EXPECTED[i] + "'");
            }
        }
    }
}
