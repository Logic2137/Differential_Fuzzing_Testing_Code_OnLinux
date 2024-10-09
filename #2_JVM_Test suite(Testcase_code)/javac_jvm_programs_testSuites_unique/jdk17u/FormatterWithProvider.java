import java.util.Formatter;
import java.util.Locale;

public class FormatterWithProvider {

    public static void main(String[] args) {
        Integer number = 1234567;
        String formatString = "%,d";
        try {
            testFormatter(Locale.JAPANESE, formatString, number);
            testFormatter(Locale.FRENCH, formatString, number);
            testFormatter(new Locale("hi", "IN"), formatString, number);
        } catch (ClassCastException ex) {
            throw new RuntimeException("[FAILED: A ClassCastException is" + " thrown while using Formatter.format() with VM" + " argument java.locale.providers=SPI,COMPAT]", ex);
        }
    }

    private static void testFormatter(Locale locale, String formatString, Integer number) {
        String.format(locale, formatString, number);
        Formatter formatter = new Formatter(locale);
        formatter.format(formatString, number);
    }
}
