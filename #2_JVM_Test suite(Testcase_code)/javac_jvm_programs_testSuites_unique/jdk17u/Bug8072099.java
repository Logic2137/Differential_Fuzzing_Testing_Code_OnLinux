import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Bug8072099 {

    private static String[][] shouldPass = { { "ha", "11AM" }, { "hma", "33AM" }, { "ka", "24AM" }, { "yyyMMM", "2016May" }, { "yyyyDDEEE", "2016366Sat" }, { "ddmyyyyz", "22111980GMT+5:30" } };

    public static void main(String[] args) {
        Locale defaultLocale = Locale.getDefault();
        try {
            Locale.setDefault(Locale.US);
            for (String[] pattern : shouldPass) {
                SimpleDateFormat dateTimeFormat = new SimpleDateFormat(pattern[0]);
                parseDateTimeInput(dateTimeFormat, pattern[1]);
            }
        } finally {
            Locale.setDefault(defaultLocale);
        }
    }

    private static void parseDateTimeInput(SimpleDateFormat format, String inputString) {
        try {
            format.parse(inputString);
        } catch (ParseException ex) {
            throw new RuntimeException("[FAILED: Unable to parse date time" + " string " + inputString + "]");
        }
    }
}
