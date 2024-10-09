import java.util.Locale;
import java.util.TimeZone;

public class HongKong {

    public static void main(String[] args) {
        Locale reservedLocale = Locale.getDefault();
        try {
            Locale.setDefault(new Locale("zh", "HK"));
            checkCountry(Locale.GERMANY, "\u5fb7\u570b");
            checkCountry(Locale.FRANCE, "\u6cd5\u570b");
            checkCountry(Locale.ITALY, "\u7fa9\u5927\u5229");
            checkTimeZone("Asia/Shanghai", "\u4e2d\u570b\u6a19\u6e96\u6642\u9593");
        } finally {
            Locale.setDefault(reservedLocale);
        }
    }

    private static void checkCountry(Locale country, String expected) {
        String actual = country.getDisplayCountry();
        if (!expected.equals(actual)) {
            throw new RuntimeException();
        }
    }

    private static void checkTimeZone(String timeZoneID, String expected) {
        TimeZone timeZone = TimeZone.getTimeZone(timeZoneID);
        String actual = timeZone.getDisplayName();
        if (!expected.equals(actual)) {
            throw new RuntimeException();
        }
    }
}
