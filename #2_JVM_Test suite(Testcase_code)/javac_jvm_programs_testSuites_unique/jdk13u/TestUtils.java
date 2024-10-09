import java.text.DecimalFormatSymbols;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Locale.Builder;

public class TestUtils {

    public static boolean usesGregorianCalendar(Locale locale) {
        return Calendar.getInstance(locale).getClass() == GregorianCalendar.class;
    }

    public static boolean usesAsciiDigits(Locale locale) {
        return DecimalFormatSymbols.getInstance(locale).getZeroDigit() == '0';
    }

    public static boolean hasSpecialVariant(Locale locale) {
        String variant = locale.getVariant();
        return !variant.isEmpty() && "JP".equals(variant) || "NY".equals(variant) || "TH".equals(variant);
    }
}
