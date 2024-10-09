import java.time.ZoneId;
import static java.util.Locale.ENGLISH;
import static java.time.format.TextStyle.FULL;
import static java.time.format.TextStyle.SHORT;

public class Bug8024141 {

    private static final String[] ZONES = { "Africa/Abidjan", "Africa/Bamako" };

    public static void main(String[] args) {
        ZoneId gmt = ZoneId.of("GMT");
        String gmtName = gmt.getDisplayName(FULL, ENGLISH);
        String gmtAbbr = gmt.getDisplayName(SHORT, ENGLISH);
        for (String zone : ZONES) {
            ZoneId id = ZoneId.of(zone);
            String name = id.getDisplayName(FULL, ENGLISH);
            String abbr = id.getDisplayName(SHORT, ENGLISH);
            if (!name.equals(gmtName) || !abbr.equals(gmtAbbr)) {
                throw new RuntimeException("inconsistent name/abbr for " + zone + ":\n" + "name=" + name + ", abbr=" + abbr);
            }
        }
    }
}
