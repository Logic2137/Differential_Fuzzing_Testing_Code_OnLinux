import java.text.DateFormatSymbols;
import java.time.chrono.HijrahChronology;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Locale;

public class Bug8152817 {

    public static void main(String[] args) throws Exception {
        System.setSecurityManager(new SecurityManager());
        DateFormatSymbols syms = DateFormatSymbols.getInstance(Locale.GERMAN);
        if (!"Oktober".equals(syms.getMonths()[Calendar.OCTOBER])) {
            throw new RuntimeException("Test failed (FormatData)");
        }
        String s = HijrahChronology.INSTANCE.getDisplayName(TextStyle.FULL, Locale.GERMAN);
        if (!s.contains("Islamischer Kalender")) {
            throw new RuntimeException("Test failed (JavaTimeSupplementary)");
        }
    }
}
