import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import static java.util.TimeZone.*;

public class Bug8141243 {

    public static void main(String[] args) {
        TimeZone UTC = TimeZone.getTimeZone("UTC");
        TimeZone initTz = TimeZone.getDefault();
        List<String> errors = new ArrayList<>();
        try {
            TimeZone.setDefault(TimeZone.getTimeZone("America/Los_Angeles"));
            for (Locale locale : DateFormat.getAvailableLocales()) {
                String utc = UTC.getDisplayName(false, SHORT, locale);
                if (!"UTC".equals(utc)) {
                    System.out.println("Skipping " + locale + " due to localized UTC name: " + utc);
                    continue;
                }
                SimpleDateFormat fmt = new SimpleDateFormat("z", locale);
                try {
                    Date date = fmt.parse("UTC");
                    if (!fmt.getTimeZone().getID().matches("(Etc/)?(UTC|Universal|UCT|Zulu)")) {
                        errors.add("timezone: " + fmt.getTimeZone().getID() + ", locale: " + locale);
                    }
                } catch (ParseException e) {
                    errors.add("parse exception: " + e + ", locale: " + locale);
                }
            }
        } finally {
            TimeZone.setDefault(initTz);
        }
        if (!errors.isEmpty()) {
            System.out.println("Got unexpected results:");
            for (String s : errors) {
                System.out.println("    " + s);
            }
            throw new RuntimeException("Test failed.");
        } else {
            System.out.println("Test passed.");
        }
    }
}
