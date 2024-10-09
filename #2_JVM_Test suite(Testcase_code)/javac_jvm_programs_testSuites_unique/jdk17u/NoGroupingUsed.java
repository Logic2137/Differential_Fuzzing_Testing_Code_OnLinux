import java.util.Formatter;
import java.util.Locale;

public class NoGroupingUsed {

    public static void main(String[] args) {
        Locale locale = new Locale("xx", "YY");
        String number = "1234567";
        String formatString = "%,d";
        try {
            testGrouping(locale, formatString, number);
        } catch (ArithmeticException ex) {
            throw new RuntimeException("[FAILED: ArithmeticException occurred" + " while formatting the number: " + number + ", with" + " format string: " + formatString + ", in locale: " + locale, ex);
        }
    }

    private static void testGrouping(Locale locale, String formatString, String number) {
        String result = String.format(locale, formatString, Integer.parseInt(number));
        if (!number.equals(result)) {
            throw new RuntimeException("[FAILED: Incorrect formatting" + " of number: " + number + " using String.format with format" + " string: " + formatString + " in locale: " + locale + ". Actual: " + result + ", Expected: " + number + "]");
        }
        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb, locale);
        formatter.format(formatString, Integer.parseInt(number));
        if (!number.equals(sb.toString())) {
            throw new RuntimeException("[FAILED: Incorrect formatting" + " of number: " + number + "using Formatter.format with" + " format string: " + formatString + " in locale: " + locale + ". Actual: " + sb.toString() + ", Expected: " + number + "]");
        }
    }
}
