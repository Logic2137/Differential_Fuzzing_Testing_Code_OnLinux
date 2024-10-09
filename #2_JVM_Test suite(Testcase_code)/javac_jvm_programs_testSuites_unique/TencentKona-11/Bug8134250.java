






import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.chrono.Chronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.FormatStyle;
import java.time.format.TextStyle;
import java.util.Locale;

public class Bug8134250 {
    public static void main(String [] args) {
        LocalDate d = LocalDate.of(1980, Month.JANUARY, 1);

        
        
        
        DateTimeFormatterBuilder dtfb = new DateTimeFormatterBuilder();
        dtfb.appendZoneText(TextStyle.SHORT);
        DateTimeFormatter dtf = dtfb.toFormatter(Locale.UK)
            .withZone(ZoneId.of("America/Los_Angeles"));
        String result = dtf.format(d);
        System.out.println(result);
        if (!"GMT-08:00".equals(result)) {
            throw new RuntimeException("short time zone name for America/Los_Angeles in en_GB is incorrect. Got: " + result + ", expected: GMT-08:00");
        }

        
        
        
        
        Locale locale = Locale.forLanguageTag("en-US-u-ca-islamic-umalqura");
        Chronology chrono = Chronology.ofLocale(locale);
        dtf = DateTimeFormatter
            .ofLocalizedDate(FormatStyle.FULL)
            .withLocale(locale)
            .withChronology(chrono);
        result = dtf.format(d);
        System.out.println(dtf.format(d));
        if (!"Tuesday, Safar 12, 1400 AH".equals(result)) {
            throw new RuntimeException("FULL date format of Islamic Um-Alqura calendar in en_US is incorrect. Got: " + result + ", expected: Tuesday, Safar 12, 1400 AH");
        }
    }
}
